package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.crafthammer.util.console.ANSIUtils
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.PacketUtils
import com.asledgehammer.craftnail.util.log.SecurityLogger
import zombie.core.raknet.UdpConnection
import zombie.core.znet.SteamUtils
import java.nio.ByteBuffer
import kotlin.collections.HashMap

/**
 * Base class for packet security checks.
 */
abstract class PacketCheck(cfg: YamlFile, val id: String) {
    
    /** The maximum distance allowed for distance-based checks. */
    protected var distance: Float = 16f
    
    private val logMessages: List<String>
    private val kickMessage: String
    private val logMessagesInServerChat: Boolean
    private val mode: Mode
    
    init {
        val cfgCheck = cfg.getSection("security_checks.$id")
        
        // Check if log_message is defined
        if (!cfgCheck.contains("log_message")) {
            throw IllegalArgumentException("'log_message' isn't defined for 'security_checks.$id' in security.yml.")
        }
        
        // Get log messages
        logMessages = if (cfgCheck.isList("log_message")) {
            cfgCheck.getStringList("log_message")
        } else {
            listOf(cfgCheck.getString("log_message"))
        }
        
        // Get log_message_in_server_chat setting
        logMessagesInServerChat = if (cfgCheck.contains("log_message_in_server_chat") && 
                                      cfgCheck.isBoolean("log_message_in_server_chat")) {
            cfgCheck.getBoolean("log_message_in_server_chat")
        } else {
            defaultLogMessagesInServerChat
        }
        
        // Get kick message
        kickMessage = if (cfgCheck.contains("kick_message") && cfgCheck.isString("kick_message")) {
            cfgCheck.getString("kick_message")
        } else {
            defaultKickMessage
        }
        
        // Get mode
        mode = if (cfgCheck.contains("mode")) {
            Mode.get(cfgCheck.getString("mode"))
        } else {
            defaultMode
        }
        
        // Get distance
        distance = if (cfgCheck.contains("distance")) {
            cfgCheck.getDouble("distance").toFloat()
        } else {
            defaultDistance
        }
        
        // Print status and increment active check count if not OFF
        printStatus()
        if (mode != Mode.OFF) {
            activeCheckCount++
        }
    }
    
    /**
     * Checks if the packet is valid according to this security check.
     * 
     * @param connection The connection that sent the packet.
     * @param buffer The packet data.
     * @return true if the packet is valid, false if it should be rejected.
     */
    fun check(connection: UdpConnection, buffer: ByteBuffer): Boolean {
        if (mode == Mode.OFF) {
            return true
        }
        
        val fields = HashMap<String, Any>()
        val originalBufferPosition = buffer.position()
        
        if (!onPacket(connection, buffer, fields)) {
            handleFailedCheck(connection, fields)
            return false
        }
        
        buffer.position(originalBufferPosition)
        return true
    }
    
    /**
     * Abstract method to be implemented by subclasses to check a specific packet type.
     * 
     * @param connection The connection that sent the packet.
     * @param buffer The packet data.
     * @param fields A map to store fields for logging purposes.
     * @return true if the packet is valid, false if it should be rejected.
     */
    protected abstract fun onPacket(
        connection: UdpConnection, 
        buffer: ByteBuffer, 
        fields: HashMap<String, Any>
    ): Boolean
    
    /**
     * Logs a violation to the security log.
     */
    protected fun log(fields: Map<String, Any>) {
        val color = if (mode == Mode.KICK) ANSIUtils.ANSI_RED else ANSIUtils.ANSI_YELLOW
        val messages = PacketChecker.injectList(logMessages, fields, color)
        
        SecurityLogger.log(messages)
        
        if (logMessagesInServerChat) {
            CraftNail.messageStaff("Security", messages)
        }
    }
    
    /**
     * Handles a failed check according to the mode.
     */
    private fun handleFailedCheck(connection: UdpConnection, fields: HashMap<String, Any>) {
        // Add player info to fields
        fields["player_id"] = if (SteamUtils.isSteamModeEnabled()) connection.steamID else connection.ip
        fields["player_username"] = connection.username
        
        // Add action type based on mode
        fields["action"] = when (mode) {
            Mode.IGNORE -> "IGNORING"
            Mode.KICK -> "KICKING"
            else -> ""
        }
        
        // Handle based on mode
        when (mode) {
            Mode.IGNORE -> log(fields)
            Mode.KICK -> {
                log(fields)
                kick(connection, fields)
            }
            else -> {} // No action for Mode.OFF
        }
    }
    
    /**
     * Kicks a player with a message.
     */
    private fun kick(connection: UdpConnection, fields: HashMap<String, Any>) {
        PacketUtils.kick(connection, PacketChecker.inject(kickMessage, fields, "", ""))
    }
    
    /**
     * Prints the status of this check to the log.
     */
    private fun printStatus() {
        var modeName = mode.name
        if (modeName.length < 6) {
            modeName += " ".repeat(6 - modeName.length)
        }
        
        val color = if (mode != Mode.OFF) ANSIUtils.ANSI_GREEN else ANSIUtils.ANSI_RED
        
        if (distance != defaultDistance) {
            CraftNail.log("Loaded Security Check: $color[$modeName] $id (distance: $distance)${ANSIUtils.ANSI_RESET}")
        } else {
            CraftNail.log("Loaded Security Check: $color[$modeName] $id${ANSIUtils.ANSI_RESET}")
        }
    }
    
    companion object {
        /** Number of active security checks. */
        var activeCheckCount: Int = 0
        
        /** Default mode for security checks. */
        lateinit var defaultMode: Mode
        
        /** Whether to log messages in server chat by default. */
        var defaultLogMessagesInServerChat: Boolean = true
        
        /** Default kick message. */
        lateinit var defaultKickMessage: String
        
        /** Default distance for distance checks. */
        var defaultDistance: Float = 16f
    }
}
