package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.crafthammer.util.console.ANSIUtils
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.TextFilter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.ArrayList
import zombie.GameWindow
import zombie.ZomboidFileSystem
import zombie.core.raknet.UdpConnection

/**
 * Main class for managing packet security checks.
 */
object PacketChecker {
    
    // Configuration constants
    private val CURRENT_CFG_VERSION = 2
    
    // Server directory
    private var dirServer = File(ZomboidFileSystem.instance.getCacheDir() + File.separator + "Server")
    
    // Configuration file
    private var cfg = YamlFile(File(dirServer, "security.yml"))
    
    // Various security check instances
    private lateinit var extraInfoVars: ExtraInfoVarsCheck
    private lateinit var extraInfoOfflinePlayer: ExtraInfoOfflinePlayerCheck
    private lateinit var teleportStaff: TeleportStaffCheck
    private lateinit var changePlayerStatsOfflinePlayer: ChangePlayerStatsOfflinePlayerCheck
    private lateinit var changePlayerStatsNonStaff: ChangePlayerStatsNonStaffCheck
    
    // Inventory management checks
    private lateinit var invMngGetItemOfflinePlayer: InvMngGetItemOfflinePlayerCheck
    private lateinit var invMngGetItemStaff: InvMngGetItemStaffCheck
    private lateinit var invMngReqItemOfflinePlayer: InvMngReqItemOfflinePlayerCheck
    private lateinit var invMngReqItemStaff: InvMngReqItemStaffCheck
    private lateinit var invMngRemoveItemOfflinePlayer: InvMngRemoveItemOfflinePlayerCheck
    private lateinit var invMngRemoveItemStaff: InvMngRemoveItemStaffCheck
    
    // Inventory request checks
    private lateinit var requestInventoryOfflinePlayer: RequestInventoryOfflinePlayerCheck
    private lateinit var requestInventoryStaff: RequestInventoryStaffCheck
    
    // Admin checks
    private lateinit var sandboxOptionsStaff: SandboxOptionsStaffCheck
    private lateinit var syncNonPvpZoneStaff: SyncNonPvpZoneStaffCheck
    
    // Chat checks
    private lateinit var chatMessageFromPlayerOtherPlayer: ChatMessageFromPlayerOtherPlayer
    
    // Medical checks
    private lateinit var bandageOfflinePlayer: BandageOfflinePlayerCheck
    private lateinit var bandageDistance: BandageDistanceCheck
    private lateinit var stitchOfflinePlayer: StitchOfflinePlayerCheck
    private lateinit var stitchDistance: StitchDistanceCheck
    private lateinit var woundInfectionOfflinePlayer: WoundInfectionOfflinePlayerCheck
    private lateinit var woundInfectionDistance: WoundInfectionDistanceCheck
    private lateinit var disinfectOfflinePlayer: DisinfectOfflinePlayerCheck
    private lateinit var disinfectDistance: DisinfectDistanceCheck
    private lateinit var splintOfflinePlayer: SplintOfflinePlayerCheck
    private lateinit var splintDistance: SplintDistanceCheck
    private lateinit var additionalPainOfflinePlayer: AdditionalPainOfflinePlayerCheck
    private lateinit var additionalPainDistance: AdditionalPainDistanceCheck
    
    // Object interaction checks
    private lateinit var removeGlassOfflinePlayer: RemoveGlassOfflinePlayerCheck
    private lateinit var removeGlassDistance: RemoveGlassDistanceCheck
    private lateinit var removeBulletOfflinePlayer: RemoveBulletOfflinePlayerCheck
    private lateinit var removeBulletDistance: RemoveBulletDistanceCheck
    private lateinit var cleanBurnOfflinePlayer: CleanBurnOfflinePlayerCheck
    private lateinit var cleanBurnDistance: CleanBurnDistanceCheck
    
    // Combat checks
    private lateinit var hitCharacterPvpDisabled: HitCharacterPvpDisabledCheck
    
    // Death and damage checks
    private lateinit var playerDeathOfflinePlayer: PlayerDeathOfflinePlayerCheck
    private lateinit var playerDeathSelf: PlayerDeathSelfCheck
    private lateinit var playerDamageOfflinePlayer: PlayerDamageOfflinePlayerCheck
    private lateinit var playerDamageDistance: PlayerDamageDistanceCheck
    
    // Fire checks
    private lateinit var startFireDisabled: StartFireDisabledCheck
    private lateinit var startFireGridSquare: StartFireGridSquareCheck
    private lateinit var startFireDistance: StartFireDistanceCheck
    
    // World object checks
    private lateinit var removeItemFromSquareGridSquare: RemoveItemFromSquareGridSquareCheck
    private lateinit var removeItemFromSquareObjectIndex: RemoveItemFromSquareObjectIndexCheck
    private lateinit var removeItemFromSquareDistanceCheck: RemoveItemFromSquareDistanceCheck
    
    // Building/destruction checks
    private lateinit var sledgehammerDestroyDisabled: SledgehammerDestroyDisabledCheck
    private lateinit var sledgehammerWeapon: SledgehammerDestroyWeaponCheck
    
    // Database checks
    private lateinit var getDBSchemaAdmin: GetDBSchemaAdminCheck
    private lateinit var getTableResultAdmin: GetTableResultAdminCheck
    private lateinit var executeQueryAdmin: ExecuteQueryAdminCheck
    
    // Weather checks
    private lateinit var climateManagerStaffCheck: ClimateManagerStaffCheck
    
    // State tracking
    private var fileReadOnce = false
    private var loadedSuccessfully = false
    
    /**
     * Initializes all security checks.
     */
    fun init() {
        // Check if config file exists, create it if needed
        if (!cfg.file!!.exists()) {
            copyFile()
        }
        
        try {
            // Read configuration
            cfg.read()
            
            // Check config version
            val cfgVersion = cfg.getInt("__meta.version")
            if (cfgVersion < CURRENT_CFG_VERSION) {
                // Configuration is outdated, create backup and use default
                val newFileName = "security_version_${cfgVersion}_${System.currentTimeMillis()}.yml"
                CraftNail.logError("security.yml is using version $cfgVersion. The current version of CraftHammer uses $CURRENT_CFG_VERSION. " +
                                "The original security.yml is copied to $newFileName. Using default configuration..")
                
                // Copy the file
                val from = cfg.file!!.toPath()
                val to = File(cfg.file!!.parent, newFileName).toPath()
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING)
                
                // Create new file and read it
                copyFile()
                cfg.read()
            }
            
            // Mark file as read
            fileReadOnce = true
            
            // Reset active check count
            PacketCheck.activeCheckCount = 0
            
            // Load default settings
            val def = cfg.getSection("security_checks.default")
            PacketCheck.defaultMode = Mode.get(def.getString("mode"))
            PacketCheck.defaultLogMessagesInServerChat = def.getBoolean("log_message_in_sever_chat")
            PacketCheck.defaultKickMessage = def.getString("kick_message")
            PacketCheck.defaultDistance = def.getDouble("distance").toFloat()
            
            // Initialize all security checks
            extraInfoOfflinePlayer = ExtraInfoOfflinePlayerCheck(cfg, "extra_info.offline_player")
            extraInfoVars = ExtraInfoVarsCheck(cfg, "extra_info.check_vars")
            teleportStaff = TeleportStaffCheck(cfg, "teleport.not_staff")
            changePlayerStatsOfflinePlayer = ChangePlayerStatsOfflinePlayerCheck(cfg, "change_player_stats.offline_player")
            changePlayerStatsNonStaff = ChangePlayerStatsNonStaffCheck(cfg, "change_player_stats.not_staff")
            invMngGetItemOfflinePlayer = InvMngGetItemOfflinePlayerCheck(cfg, "inv_mng_get_item.offline_player")
            invMngGetItemStaff = InvMngGetItemStaffCheck(cfg, "inv_mng_get_item.not_staff")
            invMngReqItemOfflinePlayer = InvMngReqItemOfflinePlayerCheck(cfg, "inv_mng_req_item.offline_player")
            invMngReqItemStaff = InvMngReqItemStaffCheck(cfg, "inv_mng_req_item.not_staff")
            invMngRemoveItemOfflinePlayer = InvMngRemoveItemOfflinePlayerCheck(cfg, "inv_mng_remove_item.offline_player")
            invMngRemoveItemStaff = InvMngRemoveItemStaffCheck(cfg, "inv_mng_remove_item.not_staff")
            requestInventoryOfflinePlayer = RequestInventoryOfflinePlayerCheck(cfg, "request_inventory.offline_player")
            requestInventoryStaff = RequestInventoryStaffCheck(cfg, "request_inventory.not_staff")
            sandboxOptionsStaff = SandboxOptionsStaffCheck(cfg, "sandbox_options.not_staff")
            syncNonPvpZoneStaff = SyncNonPvpZoneStaffCheck(cfg, "sync_non_pvp_zone.not_staff")
            chatMessageFromPlayerOtherPlayer = ChatMessageFromPlayerOtherPlayer(cfg, "chat_message_from_player.other_player")
            bandageOfflinePlayer = BandageOfflinePlayerCheck(cfg, "bandage.offline_player")
            bandageDistance = BandageDistanceCheck(cfg, "bandage.distance")
            stitchOfflinePlayer = StitchOfflinePlayerCheck(cfg, "stitch.offline_player")
            stitchDistance = StitchDistanceCheck(cfg, "stitch.distance")
            woundInfectionOfflinePlayer = WoundInfectionOfflinePlayerCheck(cfg, "wound_infection.offline_player")
            woundInfectionDistance = WoundInfectionDistanceCheck(cfg, "wound_infection.distance")
            disinfectOfflinePlayer = DisinfectOfflinePlayerCheck(cfg, "disinfect.offline_player")
            disinfectDistance = DisinfectDistanceCheck(cfg, "disinfect.distance")
            splintOfflinePlayer = SplintOfflinePlayerCheck(cfg, "splint.offline_player")
            splintDistance = SplintDistanceCheck(cfg, "splint.distance")
            additionalPainOfflinePlayer = AdditionalPainOfflinePlayerCheck(cfg, "additional_pain.offline_player")
            additionalPainDistance = AdditionalPainDistanceCheck(cfg, "additional_pain.distance")
            removeGlassOfflinePlayer = RemoveGlassOfflinePlayerCheck(cfg, "remove_glass.offline_player")
            removeGlassDistance = RemoveGlassDistanceCheck(cfg, "remove_glass.distance")
            removeBulletOfflinePlayer = RemoveBulletOfflinePlayerCheck(cfg, "remove_bullet.offline_player")
            removeBulletDistance = RemoveBulletDistanceCheck(cfg, "remove_bullet.distance")
            cleanBurnOfflinePlayer = CleanBurnOfflinePlayerCheck(cfg, "clean_burn.offline_player")
            cleanBurnDistance = CleanBurnDistanceCheck(cfg, "clean_burn.distance")
            hitCharacterPvpDisabled = HitCharacterPvpDisabledCheck(cfg, "hit_character.pvp_disabled")
            playerDeathOfflinePlayer = PlayerDeathOfflinePlayerCheck(cfg, "player_death.offline_player")
            playerDeathSelf = PlayerDeathSelfCheck(cfg, "player_death.self")
            playerDamageOfflinePlayer = PlayerDamageOfflinePlayerCheck(cfg, "player_damage.offline_player")
            playerDamageDistance = PlayerDamageDistanceCheck(cfg, "player_damage.distance")
            startFireDisabled = StartFireDisabledCheck(cfg, "start_fire.disabled")
            startFireGridSquare = StartFireGridSquareCheck(cfg, "start_fire.grid_square")
            startFireDistance = StartFireDistanceCheck(cfg, "start_fire.distance")
            removeItemFromSquareGridSquare = RemoveItemFromSquareGridSquareCheck(cfg, "remove_item_from_square.invalid_square")
            removeItemFromSquareObjectIndex = RemoveItemFromSquareObjectIndexCheck(cfg, "remove_item_from_square.object_index")
            removeItemFromSquareDistanceCheck = RemoveItemFromSquareDistanceCheck(cfg, "remove_item_from_square.distance")
            sledgehammerDestroyDisabled = SledgehammerDestroyDisabledCheck(cfg, "sledgehammer_destroy.disabled")
            sledgehammerWeapon = SledgehammerDestroyWeaponCheck(cfg, "sledgehammer_destroy.weapon")
            getDBSchemaAdmin = GetDBSchemaAdminCheck(cfg, "database.read_not_admin")
            getTableResultAdmin = GetTableResultAdminCheck(cfg, "database.read_not_admin")
            executeQueryAdmin = ExecuteQueryAdminCheck(cfg, "database.modify_not_admin")
            climateManagerStaffCheck = ClimateManagerStaffCheck(cfg, "climate_manager.not_staff")
            
            // Mark initialization as successful
            loadedSuccessfully = true
            
            // Log success
            CraftNail.log("Loaded ${PacketCheck.activeCheckCount} security check(s).")
        } catch (e: Exception) {
            // Log error
            val message = if (!fileReadOnce) {
                "Failed to parse security.yml. Security checks not active."
            } else {
                "Failed to parse security.yml. Security checks not reloaded."
            }
            CraftNail.logError(message, e)
        }
    }
    
    /**
     * Copies the default security configuration file from resources.
     */
    private fun copyFile() {
        try {
            val inputStream = javaClass.getResourceAsStream("security.yml")
            val data = ByteArray(inputStream!!.available())
            inputStream.read(data)
            inputStream.close()
            
            val fos = FileOutputStream(cfg.file!!)
            fos.write(data)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    /**
     * String replacement utility to inject values into a template string.
     * 
     * @param line The template string.
     * @param fields The fields to replace.
     * @param color The color to apply.
     * @param reset The reset code to apply at the end.
     * @return The formatted string.
     */
    fun inject(
        line: String, 
        fields: Map<String, Any>, 
        color: String = "", 
        reset: String = ANSIUtils.ANSI_RESET
    ): String {
        var msg = line
        for ((key, value) in fields) {
            msg = msg.replace("%$key%", value.toString())
        }
        return color + msg + reset
    }
    
    /**
     * Injects values into a list of template strings.
     * 
     * @param lines The template strings.
     * @param fields The fields to replace.
     * @param color The color to apply.
     * @param reset The reset code to apply at the end.
     * @return The formatted strings.
     */
    fun injectList(
        lines: List<String>, 
        fields: Map<String, Any>, 
        color: String = "", 
        reset: String = ANSIUtils.ANSI_RESET
    ): ArrayList<String> {
        val result = ArrayList<String>()
        for (line in lines) {
            result.add(inject(line, fields, color, reset))
        }
        return result
    }
    
    /* Begin packet check methods */
    
    fun checkClimateManagerPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return climateManagerStaffCheck.check(connection, buffer)
    }
    
    fun checkExecuteQueryPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return executeQueryAdmin.check(connection, buffer)
    }
    
    fun checkGetTableResultPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return getTableResultAdmin.check(connection, buffer)
    }
    
    fun checkGetDBSchemaPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return getDBSchemaAdmin.check(connection, buffer)
    }
    
    fun checkSledgehammerDestroyPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!sledgehammerDestroyDisabled.check(connection, buffer)) return false
        return sledgehammerWeapon.check(connection, buffer)
    }
    
    fun checkRemoveItemFromSquarePacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!removeItemFromSquareGridSquare.check(connection, buffer)) return false
        if (!removeItemFromSquareObjectIndex.check(connection, buffer)) return false
        return removeItemFromSquareDistanceCheck.check(connection, buffer)
    }
    
    fun checkStartFirePacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!startFireDisabled.check(connection, buffer)) return false
        if (!startFireGridSquare.check(connection, buffer)) return false
        return startFireDistance.check(connection, buffer)
    }
    
    fun checkPlayerDamagePacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!playerDamageOfflinePlayer.check(connection, buffer)) return false
        return playerDamageDistance.check(connection, buffer)
    }
    
    fun checkPlayerDeathPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!playerDeathOfflinePlayer.check(connection, buffer)) return false
        return playerDeathSelf.check(connection, buffer)
    }
    
    fun checkHitCharacterPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return hitCharacterPvpDisabled.check(connection, buffer)
    }
    
    fun checkCleanBurnPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!cleanBurnOfflinePlayer.check(connection, buffer)) return false
        return cleanBurnDistance.check(connection, buffer)
    }
    
    fun checkRemoveBulletPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!removeBulletOfflinePlayer.check(connection, buffer)) return false
        return removeBulletDistance.check(connection, buffer)
    }
    
    fun checkRemoveGlassPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!removeGlassOfflinePlayer.check(connection, buffer)) return false
        return removeGlassDistance.check(connection, buffer)
    }
    
    fun checkAdditionalPainPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!additionalPainOfflinePlayer.check(connection, buffer)) return false
        return additionalPainDistance.check(connection, buffer)
    }
    
    fun checkSplintPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!splintOfflinePlayer.check(connection, buffer)) return false
        return splintDistance.check(connection, buffer)
    }
    
    fun checkDisinfectPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!disinfectOfflinePlayer.check(connection, buffer)) return false
        return disinfectDistance.check(connection, buffer)
    }
    
    fun checkWoundInfectionPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!woundInfectionOfflinePlayer.check(connection, buffer)) return false
        return woundInfectionDistance.check(connection, buffer)
    }
    
    fun checkStitchPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!stitchOfflinePlayer.check(connection, buffer)) return false
        return stitchDistance.check(connection, buffer)
    }
    
    fun checkBandagePacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!bandageOfflinePlayer.check(connection, buffer)) return false
        return bandageDistance.check(connection, buffer)
    }
    
    fun checkChatMessageFromPlayerPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return chatMessageFromPlayerOtherPlayer.check(connection, buffer)
    }
    
    fun checkSyncNonPvpZonePacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return syncNonPvpZoneStaff.check(connection, buffer)
    }
    
    fun checkSandboxOptionsPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return sandboxOptionsStaff.check(connection, buffer)
    }
    
    fun checkRequestInventoryPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!requestInventoryOfflinePlayer.check(connection, buffer)) return false
        return requestInventoryStaff.check(connection, buffer)
    }
    
    fun checkInvMngGetItemPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!invMngGetItemOfflinePlayer.check(connection, buffer)) return false
        return invMngGetItemStaff.check(connection, buffer)
    }
    
    fun checkInvMngReqItemPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!invMngReqItemOfflinePlayer.check(connection, buffer)) return false
        return invMngReqItemStaff.check(connection, buffer)
    }
    
    fun checkInvMngRemoveItemPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!invMngRemoveItemOfflinePlayer.check(connection, buffer)) return false
        return invMngRemoveItemStaff.check(connection, buffer)
    }
    
    fun checkChangePlayerStatsPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!changePlayerStatsOfflinePlayer.check(connection, buffer)) return false
        return changePlayerStatsNonStaff.check(connection, buffer)
    }
    
    fun checkExtraInfoPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        if (!extraInfoOfflinePlayer.check(connection, buffer)) return false
        return extraInfoVars.check(connection, buffer)
    }
    
    fun checkTeleportPacket(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (!loadedSuccessfully) return true
        return teleportStaff.check(connection, buffer)
    }
    
    /**
     * Filters chat messages for banned words.
     */
    fun filterChatMessage(connection: UdpConnection, buffer: ByteBuffer) {
        val originalPosition = buffer.position()
        
        val chatBaseId = buffer.getInt()
        val messageAuthor = GameWindow.ReadString(buffer)
        val messageContent = GameWindow.ReadString(buffer)
        
        if (TextFilter.test(messageContent)) {
            val censoredMessage = TextFilter.censor(messageContent)
            
            // Reset buffer position and write the censored message
            buffer.position(originalPosition)
            buffer.putInt(chatBaseId)
            GameWindow.WriteString(buffer, messageAuthor)
            GameWindow.WriteString(buffer, censoredMessage)
            
            // Log the censored message
            val logMessage = "The player \"${connection.username}\" Tried to send message containing filter: \"$censoredMessage\". Message censored."
            CraftNail.log(logMessage)
            CraftNail.messageStaff(logMessage)
        }
        
        // Reset buffer position
        buffer.position(originalPosition)
    }
}
