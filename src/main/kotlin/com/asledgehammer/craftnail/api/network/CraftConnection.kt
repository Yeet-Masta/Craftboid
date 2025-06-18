package com.asledgehammer.craftnail.api.network

import com.asledgehammer.crafthammer.api.network.Connection
import com.asledgehammer.craftnail.api.CraftAdapter
import zombie.GameWindow
import zombie.commands.PlayerType
import zombie.core.raknet.UdpConnection
import zombie.core.znet.SteamUtils
import zombie.network.PacketTypes

class CraftConnection(handle: UdpConnection) : CraftAdapter<UdpConnection>(handle), Connection {

    override val id: String
        get() = (if (SteamUtils.isSteamModeEnabled()) handle.steamID else handle.ip) as String

    override val ownerId: Long
        get() = handle.ownerID

    override val accesslevel: Byte
        get() = handle.accessLevel

    override val accesslevelName: String
        get() = PlayerType.toString(accesslevel)

    override val username: String
        get() = handle.username

    override val guid: Long
        get() = handle.getConnectedGUID()

    override val fullyConnected: Boolean
        get() = handle.isFullyConnected()

    override val timeConnected: Long
        get() = handle.connectionTimestamp

    override fun toString(): String {
        return "CraftConnection(id=$id, username=$username, accessLevel=$accesslevel ($accesslevelName))"
    }

    override fun sendMessage(name: String, message: String) {
        val writer = handle.startPacket()
        PacketTypes.PacketType.ChatMessageToPlayer.doPacket(writer)
        writer.putInt(0)
        GameWindow.WriteStringUTF(writer.bb, name)
        GameWindow.WriteStringUTF(writer.bb, message)
        writer.putByte(0.toByte())
        PacketTypes.PacketType.ChatMessageToPlayer.send(handle)
    }

    override fun disconnect(reason: String?) {
        val writer = handle.startPacket()
        PacketTypes.PacketType.Kicked.doPacket(writer)
        writer.putUTF(reason ?: "You were disconnected from the server.")
        PacketTypes.PacketType.Kicked.send(handle)
        handle.forceDisconnect(reason)
    }
}