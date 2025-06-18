package com.asledgehammer.craftnail.player

import com.asledgehammer.crafthammer.api.Hammer
import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.Event
import com.asledgehammer.crafthammer.api.event.network.PostLoginEvent
import com.asledgehammer.crafthammer.api.event.network.PreLoginEvent
import com.asledgehammer.crafthammer.api.event.player.PlayerJoinEvent
import com.asledgehammer.crafthammer.api.event.player.PlayerQuitEvent
import com.asledgehammer.crafthammer.api.network.Connection
import com.asledgehammer.craftnail.api.entity.living.CraftPlayer
import com.asledgehammer.craftnail.api.network.CraftConnection
import java.util.Collections
import java.util.Locale
import zombie.characters.IsoPlayer
import zombie.core.raknet.UdpConnection

/**
 * Manages player tracking and events.
 */
object PlayerManager {
    // Maps for tracking players, connections, and IDs
    private val playersByIso = HashMap<IsoPlayer, Player>()
    private val isosByplayer = HashMap<Player, IsoPlayer>()
    private val playersByName = HashMap<String, Player>()
    private val playersById = HashMap<Short, Player>()
    private val playersByConnection = HashMap<Connection, Player>()
    private val connectionsByHandle = HashMap<UdpConnection, Connection>()

    /**
     * Gets all online players.
     *
     * @return An unmodifiable collection of all online players.
     */
    val onlinePlayers: Collection<Player>
        get() = Collections.unmodifiableCollection(playersByIso.values)

    /**
     * Handles the pre-login phase when a player connects.
     *
     * @param udpConnection The connection of the player.
     * @return The kick message if the login was cancelled, null otherwise.
     */
    fun onPreLogin(udpConnection: UdpConnection): String? {
        // Create a new connection
        val connection = CraftConnection(udpConnection)

        // Create and dispatch pre-login event
        val preLoginEvent = PreLoginEvent(connection)
        Hammer.instance!!.events.dispatch(preLoginEvent)

        // If login was not cancelled, store the connection
        if (!preLoginEvent.cancelled) {
            connectionsByHandle[udpConnection] = connection
        }

        // Return kick message if login was cancelled
        return preLoginEvent.kickMessage
    }

    /**
     * Handles the post-login phase when a player's connection is established.
     *
     * @param udpConnection The connection of the player.
     * @return true if the login was successful, false otherwise.
     */
    fun onPostLogin(udpConnection: UdpConnection): Boolean {
        // Get the connection
        val connection = connectionsByHandle[udpConnection]!!

        // Create and dispatch post-login event
        val postLoginEvent = PostLoginEvent(connection)
        Hammer.instance!!.events.dispatch(postLoginEvent)

        // If login was cancelled, remove the connection
        if (postLoginEvent.cancelled) {
            connectionsByHandle.remove(udpConnection)
            return false
        }

        return true
    }

    /**
     * Handles the join phase when a player joins the game.
     *
     * @param udpConnection The connection of the player.
     * @param isoPlayer The in-game player object.
     */
    fun onJoin(udpConnection: UdpConnection, isoPlayer: IsoPlayer) {
        // Get the connection
        val connection = connectionsByHandle[udpConnection]!!

        // Create player and store it in maps
        val player = CraftPlayer(connection, isoPlayer)
        playersByConnection[connection] = player
        playersByName[isoPlayer.username.lowercase(Locale.getDefault())] = player
        playersByIso[isoPlayer] = player
        isosByplayer[player] = isoPlayer
        playersById[isoPlayer.getID().toShort()] = player

        // Create and dispatch player join event
        Hammer.instance!!.events.dispatch(PlayerJoinEvent(player))
    }

    /**
     * Handles the quit phase when a player leaves the game.
     *
     * @param udpConnection The connection of the player.
     */
    fun onQuit(udpConnection: UdpConnection) {
        // Get the connection
        val connection = connectionsByHandle[udpConnection] ?: return
        connectionsByHandle.remove(udpConnection)

        // Get the player
        val player = playersByConnection[connection] ?: return
        playersByConnection.remove(connection)

        // Create and dispatch player quit event
        Hammer.instance!!.events.dispatch(PlayerQuitEvent(player))

        // Remove player from maps
        val isoPlayer = isosByplayer.remove(player) ?: return
        playersByIso.remove(isoPlayer)
        playersByName.remove(isoPlayer.username.lowercase(Locale.getDefault()))
        playersById.remove(isoPlayer.getID().toShort())
    }

    /**
     * Gets the API connection for a UdpConnection.
     *
     * @param udpConnection The UdpConnection.
     * @return The API connection.
     */
    fun getConnection(udpConnection: UdpConnection): Connection {
        return connectionsByHandle[udpConnection]!!
    }

    /**
     * Gets the API player for an IsoPlayer.
     *
     * @param isoPlayer The IsoPlayer.
     * @return The API player.
     */
    fun getPlayer(isoPlayer: IsoPlayer): Player {
        return playersByIso[isoPlayer]!!
    }

    /**
     * Gets the API player for a UdpConnection.
     *
     * @param udpConnection The UdpConnection.
     * @return The API player.
     */
    fun getPlayer(udpConnection: UdpConnection): Player {
        return playersByConnection[connectionsByHandle[udpConnection]]!!
    }
}