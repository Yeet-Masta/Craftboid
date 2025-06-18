package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.packets.DeadPlayerPacket

class PlayerDeathSelfCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val packet = DeadPlayerPacket()
        packet.parse(buffer, connection)
        
        val target = packet.getPlayer()!!
        val check = connection.username.equals(target.username, ignoreCase = true)
        
        if (!check) {
            fields["target_username"] = target.username
        }
        
        return check
    }
}
