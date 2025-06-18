package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.GameWindow
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer

class InvMngReqItemOfflinePlayerCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        if (buffer.get() == 1.toByte()) {
            GameWindow.ReadString(buffer)
        } else {
            buffer.int
        }
        
        buffer.short
        val targetId = buffer.short
        
        return GameServer.IDToPlayerMap[targetId] != null
    }
}
