package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection
import zombie.network.packets.RemoveBullet

class RemoveBulletDistanceCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val packet = RemoveBullet()
        packet.parse(buffer, connection)
        
        var check = packet.validate(connection) && packet.isConsistent()
        
        if (check) {
            packet.process()
            
            val author = packet.wielder.getPlayer()
            val target = packet.target.getPlayer()
            val bodyPart = packet.bodyPart.getBodyPart()
            
            val distanceMeasured = IsoUtils.getDistance(author, target)
            check = distance >= distanceMeasured
            
            if (!check) {
                fields["target_username"] = target.username
                fields["body_part"] = bodyPart.getType().name
                fields["distance"] = distance
                fields["distance_measured"] = distanceMeasured
            }
        }
        
        return check
    }
}
