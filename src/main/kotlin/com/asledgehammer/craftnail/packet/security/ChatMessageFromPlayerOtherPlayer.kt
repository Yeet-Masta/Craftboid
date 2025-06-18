package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.GameWindow
import zombie.core.raknet.UdpConnection

class ChatMessageFromPlayerOtherPlayer(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        buffer.int
        val targetUsername = GameWindow.ReadString(buffer)
        
        val check = targetUsername.equals(connection.username, ignoreCase = true)
        
        if (!check) {
            fields["target_username"] = targetUsername
        }
        
        return check
    }
}
