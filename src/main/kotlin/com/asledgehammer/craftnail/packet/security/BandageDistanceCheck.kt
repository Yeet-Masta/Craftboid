package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import java.util.HashMap
import zombie.characters.BodyDamage.BodyPartType
import zombie.GameWindow
import zombie.characters.IsoPlayer
import zombie.network.GameServer
import zombie.spnetwork.UdpConnection

/**
 * Check to verify that a player is within range when bandaging another player.
 * This prevents exploits where players could bandage others from anywhere on the map.
 */
class BandageDistanceCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    /**
     * Checks if the player is close enough to another player when trying to bandage them.
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
        
        // Read packet data
        val targetId = buffer.short
        val bodyPartIndex = buffer.int
        val apply = buffer.get() == 1.toByte()
        val bandageLife = buffer.float
        val isAlcoholicBandage = buffer.get() == 1.toByte()
        val bandageType = GameWindow.ReadStringUTF(buffer)
        
        // Get players
        val author = GameServer.getAnyPlayerFromConnection(connection)!!
        val target = GameServer.IDToPlayerMap[targetId]!!
        
        // Calculate distance
        val distanceMeasured = IsoUtils.getDistance(author, target as IsoPlayer)
        
        // Check if within range
        val check = distance >= distanceMeasured
        
        // If too far, add fields for logging
        if (!check) {
            fields["target_username"] = target.username
            fields["body_part"] = BodyPartType.FromIndex(bodyPartIndex).name
            fields["apply"] = apply
            fields["bandage_life"] = bandageLife
            fields["is_alcoholic_bandage"] = isAlcoholicBandage
            fields["bandage_type"] = bandageType
            fields["distance"] = distance
            fields["distance_measured"] = distanceMeasured
        }
        
        return check
    }
}
