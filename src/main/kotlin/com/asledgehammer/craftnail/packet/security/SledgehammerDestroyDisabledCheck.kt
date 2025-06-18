package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection
import zombie.network.ServerOptions

class SledgehammerDestroyDisabledCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val check = ServerOptions.instance.AllowDestructionBySledgehammer.getValue()
        
        if (!check) {
            fields["x"] = buffer.int
            fields["y"] = buffer.int
            fields["z"] = buffer.int
            fields["object_index"] = buffer.int
        }
        
        return check
    }
}
