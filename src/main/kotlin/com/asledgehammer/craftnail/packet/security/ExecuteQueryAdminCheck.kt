package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.GameWindow
import zombie.Lua.LuaManager
import zombie.core.raknet.UdpConnection
import se.krka.kahlua.vm.KahluaTable
import se.krka.kahlua.vm.KahluaTableIterator

class ExecuteQueryAdminCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        val check = connection.accessLevel.toInt() == 32
        
        if (!check) {
            val queryOriginal = GameWindow.ReadString(buffer)
            
            try {
                val table = LuaManager.platform.newTable()
                table.load(buffer, 186)
                
                var query = queryOriginal
                val iterator = table.iterator()
                
                while (iterator.advance()) {
                    val value = iterator.getValue()
                    
                    query = if (value is Number || value is Boolean) {
                        query.replaceFirst("?", value.toString())
                    } else {
                        query.replaceFirst("?", "'${value as String}'")
                    }
                }
                
                fields["query"] = query
                
            } catch (e: Exception) {
                fields["query"] = "$queryOriginal (Invalid Args)"
            }
        }
        
        return check
    }
}
