package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer

class PlayerDamageDistanceCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val targetId = buffer.short
        val damageAmount = buffer.float
        
        val author = GameServer.getAnyPlayerFromConnection(connection)
        val target = GameServer.IDToPlayerMap[targetId] as IsoPlayer
        
        val distanceMeasured = IsoUtils.getDistance(author, target)
        val check = distance >= distanceMeasured
        
        if (!check) {
            fields["target_username"] = target.username
            fields["damage_amount"] = damageAmount
            fields["distance"] = distance
            fields["distance_measured"] = distanceMeasured
        }
        
        return check
    }
}
