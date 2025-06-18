package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import zombie.network.GameServer
import zombie.spnetwork.UdpConnection
import java.nio.ByteBuffer
import java.util.HashMap
import java.util.Locale

/**
 * Check to verify that a player is using a sledgehammer to destroy objects.
 * This prevents players from destroying objects without the proper tool.
 */
class SledgehammerDestroyWeaponCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    /**
     * Checks if the player has a sledgehammer in their hand when attempting to destroy something.
     * 
     * @param connection The connection sending the packet.
     * @param buffer The packet data.
     * @param fields Map to store fields for logging.
     * @return true if the check passes, false otherwise.
     */
    override fun onPacket(
        connection: zombie.core.raknet.UdpConnection,
        buffer: ByteBuffer,
        fields: kotlin.collections.HashMap<String, Any>
    ): Boolean {
        // Staff bypass
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        // Get the player
        val player = GameServer.getAnyPlayerFromConnection(connection)!!
        
        // Get the primary hand item
        val item = player.primaryHandItem
        
        // Get the item name
        val itemName = item?.scriptItem?.name
        
        // Check if the item name contains "sledgehammer"
        val isSledgehammer = itemName?.lowercase(Locale.ROOT)?.contains("sledgehammer") ?: false
        
        // If not a sledgehammer, add fields for logging
        if (!isSledgehammer) {
            // Add coordinate fields
            fields["x"] = buffer.int
            fields["y"] = buffer.int
            fields["z"] = buffer.int
            fields["object_index"] = buffer.int
            
            // Add weapon information
            fields["weapon"] = itemName ?: "bare hands"
        }
        
        // Return true if sledgehammer, false otherwise
        return isSledgehammer
    }
}
