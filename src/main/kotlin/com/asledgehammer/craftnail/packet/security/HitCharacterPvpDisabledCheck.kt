package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection
import zombie.network.packets.hit.HitCharacterPacket

class HitCharacterPvpDisabledCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        return CraftNail.isStaff(connection) || 
               CraftNail.isPvpEnabled() || 
               buffer.get() != HitCharacterPacket.HitType.PlayerHitPlayer.ordinal.toByte()
    }
}
