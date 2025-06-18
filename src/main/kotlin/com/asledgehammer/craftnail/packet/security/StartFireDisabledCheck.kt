package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection
import zombie.network.ServerOptions
import zombie.network.packets.StartFire

class StartFireDisabledCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val packet = StartFire()
        packet.parse(buffer, connection)
        
        var check = packet.isConsistent() && packet.validate(connection)
        
        if (check) {
            packet.process()
            
            check = !ServerOptions.instance.NoFire.getValue()
            
            if (!check) {
                fields["x"] = packet.square.getX()
                fields["y"] = packet.square.getY()
                fields["z"] = packet.square.getZ()
                fields["energy"] = packet.fireEnergy
                fields["life"] = packet.life
                fields["smoke"] = packet.smoke
            }
        }
        
        return check
    }
}
