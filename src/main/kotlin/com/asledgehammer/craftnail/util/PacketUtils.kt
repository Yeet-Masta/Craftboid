package com.asledgehammer.craftnail.util

import zombie.core.network.ByteBufferWriter
import zombie.core.raknet.UdpConnection
import zombie.network.PacketTypes

object PacketUtils {
    
    fun kick(connection: UdpConnection, reason: String?) {
        val writer = connection.startPacket()
        PacketTypes.PacketType.Kicked.doPacket(writer)
        writer.putUTF(reason)
        PacketTypes.PacketType.Kicked.send(connection)
        connection.forceDisconnect(reason ?: "Generic")
    }
}