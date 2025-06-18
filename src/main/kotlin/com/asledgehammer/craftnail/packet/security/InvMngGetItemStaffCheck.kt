package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer

class InvMngGetItemStaffCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val targetId = buffer.short
        val target = GameServer.IDToPlayerMap[targetId] as IsoPlayer
        
        fields["target_username"] = target.username
        
        return false
    }
}
