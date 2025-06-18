package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer
import zombie.network.packets.StartFire

class StartFireDistanceCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
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
            
            val x = packet.square.getX()
            val y = packet.square.getY()
            val z = packet.square.getZ()
            
            val author = GameServer.getAnyPlayerFromConnection(connection) as IsoPlayer
            
            val distanceMeasured = IsoUtils.getDistance(author, x, y, z)
            check = distance >= distanceMeasured
            
            if (!check) {
                fields["x"] = x
                fields["y"] = y
                fields["z"] = z
                fields["energy"] = packet.fireEnergy
                fields["life"] = packet.life
                fields["smoke"] = packet.smoke
                fields["distance"] = distance
                fields["distance_measured"] = distanceMeasured
            }
        }
        
        return check
    }
}
