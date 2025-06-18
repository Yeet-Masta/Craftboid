package com.asledgehammer.craftnail

import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.log.LogListener
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.crafthammer.util.console.ANSIUtils
import com.asledgehammer.craftnail.hook.CraftHookManager
import com.asledgehammer.craftnail.packet.security.PacketChecker
import com.asledgehammer.craftnail.player.PlayerManager
import com.asledgehammer.craftnail.util.TextFilter
import com.asledgehammer.craftnail.util.log.SecurityLogger
import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.crafthammer.CraftHammerLangPack
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import zombie.ZomboidFileSystem
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.debug.DebugLog
import zombie.debug.DebugType
import zombie.network.GameServer
import zombie.network.ServerOptions

/**
 * Main class for the CraftNail system.
 */
object CraftNail {
    /** Version of the mod. */
    const val VERSION = "1.0.6_00"
    
    /** Beta version number. */
    const val BETA_VERSION = "1"
    
    /** Whether this is a beta version. */
    const val IS_BETA = true
    
    /** The language pack for localization. */
    var lang: LangPack = CraftHammerLangPack(this::class.java.classLoader)
    
    /** The cache directory. */
    var dirCache: File = File(ZomboidFileSystem.instance.getCacheDir())
    
    /** The server cache directory. */
    var dirCacheServer: File = File(dirCache, "Server")
    
    /** The main configuration file. */
    var cfg: YamlFile = YamlFile(File(dirCacheServer, "crafthammer.yml"))
    
    /**
     * Gets all online players.
     */
    val onlinePlayers: Collection<Player>
        get() = PlayerManager.onlinePlayers
    
    /**
     * Initializes the CraftNail system.
     */
    fun init() {
        log("Init..")
        
        // Reset language pack
        lang.clear()
        
        // Set up directories
        dirCache = File(ZomboidFileSystem.instance.getCacheDir())
        dirCacheServer = File(dirCache, "Server")
        cfg = YamlFile(File(dirCacheServer, "crafthammer.yml"))
        
        // Initialize components
        TextFilter.init()
        PacketChecker.init()
        SecurityLogger.init()
        
        // Load configuration
        loadConfig()
        
        // Load hooks
        log("Loading CraftHook(s)..")
        CraftHookManager.invoke(cfg.getStringList("hooks"))
        CraftHookManager.loadAll()
        log("Loaded ${CraftHookManager.size()} CraftHook(s).")
    }
    
    /**
     * Starts the CraftNail system.
     */
    fun start() {
        CraftHookManager.enableAll()
        log("Enabled ${CraftHookManager.size()} CraftHook(s).")
    }
    
    /**
     * Ticks the CraftNail system.
     */
    fun tick() {
        CraftHookManager.tick()
    }
    
    /**
     * Stops the CraftNail system.
     */
    fun stop() {
        CraftHookManager.disableAll()
        log("Disabled ${CraftHookManager.size()} CraftHook(s).")
        
        CraftHookManager.unloadAll()
        log("Unloaded ${CraftHookManager.size()} CraftHook(s).")
        
        CraftHookManager.clear()
        SecurityLogger.stop()
    }
    
    /**
     * Sends messages to all staff members.
     * 
     * @param name The name of the sender.
     * @param messages The messages to send.
     */
    fun messageStaff(name: String, messages: Collection<String>) {
        val players = onlinePlayers.filter { it.isStaff }
        
        for (player in players) {
            player.sendMessages(name, *messages.toTypedArray())
        }
    }
    
    /**
     * Sends messages to all staff members.
     * 
     * @param name The name of the sender.
     * @param messages The messages to send.
     */
    fun messageStaff(name: String, vararg messages: String) {
        val players = onlinePlayers.filter { it.isStaff }
        
        for (player in players) {
            player.sendMessages(name, *messages)
        }
    }
    
    /**
     * Logs messages to the console.
     * 
     * @param list The list of objects to log.
     */
    fun log(list: List<Any?>) {
        if (list.isEmpty()) {
            DebugLog.log(DebugType.CraftHammer, "")
            return
        }
        
        for (o in list) {
            if (o == null) {
                DebugLog.log(DebugType.CraftHammer, "")
            } else {
                DebugLog.log(DebugType.CraftHammer, o.toString())
            }
        }
    }
    
    /**
     * Logs messages to the console.
     * 
     * @param objects The objects to log.
     */
    fun log(vararg objects: Any?) {
        if (objects.isEmpty()) {
            DebugLog.log(DebugType.CraftHammer, "")
            return
        }
        
        for (o in objects) {
            if (o == null) {
                DebugLog.log(DebugType.CraftHammer, "")
            } else {
                DebugLog.log(DebugType.CraftHammer, o.toString())
            }
        }
    }
    
    /**
     * Logs an error message to the console.
     * 
     * @param message The error message.
     * @param throwable The throwable that caused the error, if any.
     */
    @JvmOverloads
    fun logError(message: String, throwable: Throwable? = null) {
        val red = ANSIUtils.ANSI_BRIGHT_RED
        val reset = ANSIUtils.ANSI_RESET
        
        DebugLog.log(DebugType.Sledgehammer, "$red$message$reset")
        
        if (throwable != null) {
            logError(red, reset, throwable)
        }
    }
    
    /**
     * Recursively logs throwable information.
     */
    private fun logError(red: String, reset: String, t: Throwable) {
        DebugLog.log(DebugType.Sledgehammer, "$red${t.javaClass.name}: ${t.message}$reset")
        
        for (elm in t.stackTrace) {
            DebugLog.log(DebugType.Sledgehammer, "$red at $elm$reset")
        }
        
        t.cause?.let { logError(red, reset, it) }
    }
    
    /**
     * Adds a log listener.
     * 
     * @param id The ID of the listener.
     * @param listener The listener to add.
     */
    fun addLogListener(id: UUID, listener: LogListener) {
        DebugLog.addListener(id, listener)
    }
    
    /**
     * Removes a log listener.
     * 
     * @param id The ID of the listener.
     * @param listener The listener to remove.
     */
    fun removeLogListener(id: UUID, listener: LogListener) {
        DebugLog.removeListener(id, listener)
    }
    
    /**
     * Removes all log listeners with the given ID.
     * 
     * @param id The ID of the listeners to remove.
     */
    fun removeLogListeners(id: UUID) {
        DebugLog.removeListeners(id)
    }
    
    /**
     * Checks if a player has staff privileges.
     * 
     * @param player The player to check.
     * @return true if the player has staff privileges, false otherwise.
     */
    fun isStaff(player: IsoPlayer?): Boolean {
        return isStaff(GameServer.getConnectionFromPlayer(player))
    }
    
    /**
     * Checks if a connection has staff privileges.
     * 
     * @param connection The connection to check.
     * @return true if the connection has staff privileges, false otherwise.
     */
    fun isStaff(connection: UdpConnection?): Boolean {
        return connection != null && connection.accessLevel > 1
    }
    
    /**
     * Prints the CraftNail logo to the console.
     * 
     * @param delay Whether to delay after printing.
     */
    //@JvmOverloads
    fun printLogo(delay: Boolean = true) {
        val bw = ANSIUtils.ANSI_BRIGHT_WHITE
        val w = ANSIUtils.ANSI_WHITE
        val bold = ANSIUtils.ANSI_BOLD
        val under = ANSIUtils.ANSI_UNDERLINE
        
        val versionLine = "Version $VERSION (BETA $BETA_VERSION)             By The Bitch that Cracked your fucking software!!!"
        val discordLine = "  Join our Discord Server: $under" + "https://discord.gg/lickmyassbitch"
        
        val logo = """
            
            @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            @@                                                                                                                 @@
            @@                                                                                                                 @@
            @@${bw}     ######  ########     ###    ######## ######## ##     ##    ###    ##     ## ##     ## ######## ########  ${w}   @@
            @@${bw}    ##    ## ##     ##   ## ##   ##          ##    ##     ##   ## ##   ###   ### ###   ### ##       ##     ## ${w}   @@
            @@${bw}    ##       ##     ##  ##   ##  ##          ##    ##     ##  ##   ##  #### #### #### #### ##       ##     ## ${w}   @@
            @@${bw}    ##       ########  ##     ## ######      ##    ######### ##     ## ## ### ## ## ### ## ######   ########  ${w}   @@
            @@${bw}    ##       ##   ##   ######### ##          ##    ##     ## ######### ##     ## ##     ## ##       ##   ##   ${w}   @@
            @@${bw}    ##    ## ##    ##  ##     ## ##          ##    ##     ## ##     ## ##     ## ##     ## ##       ##    ##  ${w}   @@
            @@${bw}     ######  ##     ## ##     ## ##          ##    ##     ## ##     ## ##     ## ##     ## ######## ##     ## ${w}   @@
            @@                                                                                                                 @@
            @@${bold}                           ${ANSIUtils.wrapRGB8(versionLine, 255)}                                @@
            @@                                                                                                                 @@
            @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            
            
        """.trimIndent().replace("#", "#")
        
        val lines = logo.split("\r\n")
        
        println(ANSIUtils.ANSI_CLEAR_SCREEN)
        
        for (line in lines) {
            println(ANSIUtils.grayScale8(line, '@', '#', 240))
        }
        
        println("${ANSIUtils.wrapRGB8(discordLine, 250)}\n\n")
        
        if (delay) {
            Thread.sleep(1000)
        }
    }
    
    /**
     * Loads the CraftNail configuration.
     */
    private fun loadConfig() {
        if (!cfg.file!!.exists()) {
            writeConfig()
        }
        
        cfg.read()
    }
    
    /**
     * Writes the default CraftNail configuration.
     */
    private fun writeConfig() {
        try {
            val inputStream = javaClass.getResourceAsStream("crafthammer.yml")
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
     * Checks if PVP is enabled on the server.
     * 
     * @return true if PVP is enabled, false otherwise.
     */
    fun isPvpEnabled(): Boolean {
        return !ServerOptions.instance.PVP.value
    }
}