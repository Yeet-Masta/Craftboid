package com.asledgehammer.craftnail.util

import com.asledgehammer.craftnail.CraftNail
import zombie.characters.IsoPlayer
import zombie.core.network.ByteBufferWriter
import zombie.core.raknet.UdpConnection
import zombie.debug.DebugLog
import zombie.debug.DebugType
import zombie.iso.IsoGridSquare
import zombie.iso.areas.SafeHouse
import zombie.network.GameServer
import zombie.network.PacketTypes
import zombie.network.ServerMap
import zombie.util.list.PZArrayList

/**
 * Utility functions for working with the game world.
 */
object IsoUtils {
    
    /**
     * Gets the distance between two players.
     * 
     * @param player1 The first player.
     * @param player2 The second player.
     * @return The distance between the players.
     */
    fun getDistance(player1: IsoPlayer, player2: IsoPlayer): Float {
        // If it's the same player, distance is 0
        if (player1 == player2) {
            return 0f
        }
        
        // Use the game's distance calculation
        return zombie.iso.IsoUtils.DistanceTo(
            player1.x, player1.y, player1.z,
            player2.x, player2.y, player2.z
        )
    }
    
    /**
     * Gets the distance between a player and a position.
     * 
     * @param player The player.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The distance between the player and the position.
     */
    fun getDistance(player: IsoPlayer, x: Float, y: Float, z: Float): Float {
        return zombie.iso.IsoUtils.DistanceTo(
            player.x, player.y, player.z,
            x, y, z
        )
    }
    
    /**
     * Gets the distance between two positions.
     * 
     * @param x1 The x coordinate of the first position.
     * @param y1 The y coordinate of the first position.
     * @param z1 The z coordinate of the first position.
     * @param x2 The x coordinate of the second position.
     * @param y2 The y coordinate of the second position.
     * @param z2 The z coordinate of the second position.
     * @return The distance between the positions.
     */
    fun getDistance(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Float {
        return zombie.iso.IsoUtils.DistanceTo(x1, y1, z1, x2, y2, z2)
    }
    
    /**
     * Gets a grid square at the specified position.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @return The grid square at the position, or null if none exists.
     */
    fun getGridSquare(x: Int, y: Int, z: Int): IsoGridSquare? {
        return ServerMap.instance.getGridSquare(x, y, z)
    }
    
    /**
     * Gets the safe house owned by a player.
     * 
     * @param player The player.
     * @return The safe house owned by the player, or null if none exists.
     */
    fun getSafeHouse(player: IsoPlayer): SafeHouse? {
        var safeHouse: SafeHouse? = null
        
        // Look for a safehouse with this player as owner
        for (nextHouse in SafeHouse.getSafehouseList()) {
            if (nextHouse.owner.equals(player.username, ignoreCase = true)) {
                safeHouse = nextHouse
                break
            }
        }
        
        return safeHouse
    }
    
    /**
     * Checks if a position is inside a safe house.
     * 
     * @param safeHouse The safe house.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if the position is inside the safe house, false otherwise.
     */
    fun isInside(safeHouse: SafeHouse, x: Int, y: Int): Boolean {
        val x1 = safeHouse.x
        val y1 = safeHouse.y
        val x2 = safeHouse.x2
        val y2 = safeHouse.y2
        
        return x in x1 until x2 && y in y1 until y2
    }
    
    /**
     * Checks if a position is inside a safe house.
     * 
     * @param safeHouse The safe house.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return true if the position is inside the safe house, false otherwise.
     */
    fun isInside(safeHouse: SafeHouse, x: Float, y: Float): Boolean {
        return isInside(safeHouse, Math.floor(x.toDouble()).toInt(), Math.floor(y.toDouble()).toInt())
    }
    
    /**
     * Checks if a player is inside a safe house.
     * 
     * @param safeHouse The safe house.
     * @param player The player.
     * @return true if the player is inside the safe house, false otherwise.
     */
    fun isInside(safeHouse: SafeHouse, player: IsoPlayer): Boolean {
        return isInside(safeHouse, player.x, player.y)
    }
    
    /**
     * Checks if a player is outside a certain distance from another player.
     * 
     * @param player1 The first player.
     * @param player2 The second player.
     * @param distance The distance threshold.
     * @return true if the players are further apart than the distance, false otherwise.
     */
    fun isOutsideDistance(player1: IsoPlayer, player2: IsoPlayer, distance: Float): Boolean {
        if (player1 != player2) {
            if (distance <= getDistance(player1, player2)) {
                return true
            }
        }
        return false
    }
    
    /**
     * Teleports a player to a position.
     * 
     * @param player The player to teleport.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    fun teleport(player: IsoPlayer?, x: Float, y: Float, z: Float) {
        if (player == null) {
            throw NullPointerException("The IsoPlayer given is null.")
        }
        
        // Set player position
        player.setX(x)
        player.setY(y)
        player.setZ(z)
        
        // Get player connection
        val conn = GameServer.getConnectionFromPlayer(player)
        if (conn == null) {
            DebugLog.log(DebugType.Network, "Attempted to teleport player '${player.username}' when offline. Ignoring..")
            return
        }
        
        // Send teleport packet
        val bb = conn.startPacket()
        PacketTypes.PacketType.Teleport.doPacket(bb)
        bb.putByte(0.toByte())
        bb.putFloat(x)
        bb.putFloat(y)
        bb.putFloat(z)
        PacketTypes.PacketType.Teleport.send(conn)
    }
    
    /**
     * Kicks a player out of a safe house physically (teleports them outside).
     * 
     * @param safeHouse The safe house.
     * @param player The player to kick.
     */
    fun kickPlayerFromSafeHousePhysically(safeHouse: SafeHouse, player: IsoPlayer?) {
        if (player == null) {
            throw NullPointerException("The IsoPlayer given is null.")
        }
        
        // Calculate position outside the safehouse
        val toX = safeHouse.x - 1f
        val toY = safeHouse.y - 1f
        
        // Check if player is inside the safehouse
        if (isInside(safeHouse, player)) {
            // Teleport them outside
            teleport(player, toX, toY, player.z)
            
            DebugLog.log(
                DebugType.CraftHammer, 
                "Gracefully kicked out player '${player.username}' from SafeHouse at {x: ${safeHouse.x}, y: ${safeHouse.y}}."
            )
        }
    }
    
    /**
     * Converts a PZArrayList to an array.
     * 
     * @param list The list to convert.
     * @param type The type of elements in the list.
     * @return An array containing the elements of the list.
     */
    fun <E> toArray(list: PZArrayList<E>, type: Class<E>): Array<E> {
        @Suppress("UNCHECKED_CAST")
        val array = arrayOfNulls<Any>(list.size) as Array<E>
        
        for (i in 0 until list.size) {
            array[i] = list[i]
        }
        
        return array
    }
}