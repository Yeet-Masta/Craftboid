package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.GameWindow
import zombie.core.raknet.UdpConnection

class TeleportStaffCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        val check = CraftNail.isStaff(connection)
        
        if (!check) {
            fields["target_username"] = GameWindow.ReadString(buffer)
            fields["x"] = buffer.float
            fields["y"] = buffer.float
            fields["z"] = buffer.float
        }
        
        return check
    }
}
