package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.craftnail.util.IsoUtils
import java.nio.ByteBuffer
import zombie.core.raknet.UdpConnection

class RemoveItemFromSquareGridSquareCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val x = buffer.int
        val y = buffer.int
        val z = buffer.int
        
        val check = IsoUtils.getGridSquare(x, y, z) != null
        
        if (!check) {
            fields["x"] = x
            fields["y"] = y
            fields["z"] = z
            fields["object_index"] = buffer.int
        }
        
        return check
    }
}
