package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.characters.BodyDamage.BodyPartType
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer

class SplintDistanceCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val targetId = buffer.short
        val bodyPartIndex = buffer.int
        val flag = buffer.get() == 1.toByte()
        
        val author = GameServer.getAnyPlayerFromConnection(connection) as IsoPlayer
        val target = GameServer.IDToPlayerMap[targetId] as IsoPlayer
        
        val distanceMeasured = IsoUtils.getDistance(author, target)
        val check = distance >= distanceMeasured
        
        if (!check) {
            fields["target_username"] = target.username
            fields["body_part"] = BodyPartType.FromIndex(bodyPartIndex).name
            fields["flag"] = flag
            fields["distance"] = distance
            fields["distance_measured"] = distanceMeasured
        }
        
        return check
    }
}
