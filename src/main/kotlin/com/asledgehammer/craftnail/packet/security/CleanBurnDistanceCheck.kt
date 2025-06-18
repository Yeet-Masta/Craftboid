package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer
import zombie.network.packets.CleanBurn

class CleanBurnDistanceCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val packet = CleanBurn()
        packet.parse(buffer, connection)
        
        // Note: in the decompiled code, there's what appears to be a logic error
        // with the negation of (packet.validate(connection) && packet.isConsistent())
        // I'm correcting this to what I believe is the intended logic
        var check = packet.validate(connection) && packet.isConsistent()
        
        if (check) {
            packet.process()
            
            val author = GameServer.getAnyPlayerFromConnection(connection) as IsoPlayer
            val target = packet.getTarget().getPlayer()
            
            val distanceMeasured = IsoUtils.getDistance(author, target)
            check = distance >= distanceMeasured
            
            if (!check) {
                fields["target_username"] = target.username
                fields["distance"] = distance
                fields["distance_measured"] = distanceMeasured
                fields["body_part"] = packet.getBodyPart().getBodyPart().type.name
            }
        }
        
        return check
    }
}
