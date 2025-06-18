package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.characters.BodyDamage.BodyPartType
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer

class WoundInfectionOfflinePlayerCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val targetId = buffer.short
        val bodyPartIndex = buffer.int
        val flag = buffer.get() == 1.toByte()
        
        val check = GameServer.IDToPlayerMap[targetId] != null
        
        if (!check) {
            fields["body_part"] = BodyPartType.FromIndex(bodyPartIndex).name
            fields["flag"] = flag
        }
        
        return check
    }
}
