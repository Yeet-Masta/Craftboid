package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection
import zombie.network.packets.Disinfect

class DisinfectOfflinePlayerCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val packet = Disinfect()
        packet.parse(buffer, connection)
        
        var check = packet.validate(connection) && packet.isConsistent()
        
        if (check) {
            check = packet.getTarget().getPlayer() != null
            
            if (!check) {
                fields["body_part"] = packet.getBodyPart().getBodyPart().getType().name
                fields["alcohol_level_to_add"] = packet.getAlcohol().getItem().getAlcoholPower()
            }
        }
        
        return check
    }
}
