package com.asledgehammer.craftnail.packet.security

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.nio.ByteBuffer
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection
import zombie.network.GameServer

class ExtraInfoVarsCheck(cfg: YamlFile, id: String) : PacketCheck(cfg, id) {
    
    override fun onPacket(
        connection: UdpConnection,
        buffer: ByteBuffer,
        fields: HashMap<String, Any>
    ): Boolean {
        if (CraftNail.isStaff(connection)) {
            return true
        }
        
        val playerID = buffer.short
        val isGodModEnabled = buffer.get() == 1.toByte()
        val isGhostModeEnabled = buffer.get() == 1.toByte()
        val isInvisibleEnabled = buffer.get() == 1.toByte()
        val isNoClipEnabled = buffer.get() == 1.toByte()
        val isShowAdminTagEnabled = buffer.get() == 1.toByte()
        val isCanHearAllEnabled = buffer.get() == 1.toByte()
        
        val player = GameServer.IDToPlayerMap[playerID] as IsoPlayer
        
        val pass = (!player.isGodMod() && isGodModEnabled) || 
                   (!player.isGhostMode() && isGhostModeEnabled) || 
                   (!player.isInvisible() && isInvisibleEnabled) || 
                   (!player.isNoClip() && isNoClipEnabled) || 
                   (!player.isShowAdminTag() && isShowAdminTagEnabled) || 
                   (!player.isCanHearAll() && isCanHearAllEnabled)
        
        if (!pass) {
            fields["target_username"] = player.username
            fields["packet_is_god_mod_enabled"] = isGodModEnabled
            fields["packet_is_ghost_mode_enabled"] = isGhostModeEnabled
            fields["packet_is_invisible_enabled"] = isInvisibleEnabled
            fields["packet_is_no_clip_enabled"] = isNoClipEnabled
            fields["packet_is_show_admin_tag_enabled"] = isShowAdminTagEnabled
            fields["packet_is_can_hear_all_enabled"] = isCanHearAllEnabled
            fields["player_is_god_mod_enabled"] = player.isGodMod()
            fields["player_is_ghost_mode_enabled"] = player.isGhostMode()
            fields["player_is_invisible_enabled"] = player.isInvisible()
            fields["player_is_no_clip_enabled"] = player.isNoClip()
            fields["player_is_show_admin_tag_enabled"] = player.isShowAdminTag()
            fields["player_is_can_hear_all_enabled"] = player.isCanHearAll()
        }
        
        return pass
    }
}
