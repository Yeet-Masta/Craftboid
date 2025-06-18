package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection
import zombie.network.packets.StartFire

class StartFireGridSquareCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
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
            
            val x = packet.square.getX().toInt()
            val y = packet.square.getY().toInt()
            val z = packet.square.getZ().toInt()
            
            check = IsoUtils.getGridSquare(x, y, z) != null
            
            if (!check) {
                fields["x"] = x
                fields["y"] = y
                fields["z"] = z
                fields["energy"] = packet.fireEnergy
                fields["life"] = packet.life
                fields["smoke"] = packet.smoke
            }
        }
        
        return check
    }
}
