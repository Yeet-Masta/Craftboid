package com.asledgehammer.craftnail.api.entity.living

import com.asledgehammer.crafthammer.api.Hammer
import com.asledgehammer.crafthammer.api.entity.AccessLevel
import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.network.Connection
import com.asledgehammer.crafthammer.api.permission.PermissionGroup
import com.asledgehammer.crafthammer.api.permission.PermissionUser
import com.asledgehammer.crafthammer.api.permission.Permissions
import com.asledgehammer.crafthammer.util.component.TextComponent
import com.asledgehammer.craftnail.api.CraftAdapter
import org.joml.Vector3f
import zombie.characters.IsoPlayer
import zombie.network.GameServer

class CraftPlayer(
    override val connection: Connection,
    handle: IsoPlayer
) : CraftAdapter<IsoPlayer>(handle), Player {

    override val location: Vector3f
        get() = Vector3f(handle.x, handle.y, handle.z)

    override val index: Short
        get() = handle.PlayerIndex.toShort()

    override val username: String
        get() = handle.username ?: ""

    override val displayName: String
        get() = handle.displayName ?: username

    override val accessLevel: AccessLevel
        get() = AccessLevel.get(handle.accessLevel)

    override val isStaff: Boolean
        get() = accessLevel != AccessLevel.NONE

    override val online: Boolean
        get() = connection.fullyConnected

    override val alive: Boolean
        get() = handle.isAlive()

    override fun toString(): String {
        return "CraftPlayer(index=$index, name='$username', accessLevel=$accessLevel, isStaff=$isStaff, isOnline=$online)"
    }

    override fun disconnect(reason: String?) {
        connection.disconnect(reason)
    }

    override fun sendMessage(name: String?, message: String) {
        if (online) {
            connection.sendMessage(name ?: "Server", message)
        }
    }

    override fun sendMessage(name: String?, message: TextComponent) {
        if (online) {
            connection.sendMessage(name ?: "Server", message.format(TextComponent.Format.CHAT))
        }
    }

    override fun hasPermission(context: String): Boolean {
        if (accessLevel == AccessLevel.ADMIN) {
            return true
        }
        
        val permissions = Hammer.instance?.permissions ?: return false
        
        if (permissions.hasUser(username)) {
            val pUser = permissions.getUser(username)
            
            if (pUser.has(context)) {
                return pUser.get(context).flag
            }
            
            val pGroup = pUser.group
            if (pGroup != null && pGroup.has(context)) {
                return pGroup.get(context).flag
            }
        }
        
        val dGroup = permissions.defaultGroup
        if (dGroup.has(context)) {
            return dGroup.get(context).flag
        }
        
        return false
    }

    override fun setPermission(context: String, flag: Boolean) {
        val permissions = Hammer.instance?.permissions ?: return
        
        val user = if (permissions.hasUser(username)) {
            permissions.getUser(username)
        } else {
            permissions.createUser(username)
        }
        
        user.set(context, flag)
        permissions.save()
    }

    override fun teleport(location: Vector3f) {
        handle.setX(location.x)
        handle.setY(location.y)
        handle.setZ(location.z)
        GameServer.sendTeleport(handle, location.x, location.y, location.z)
    }

    override fun teleport(x: Float, y: Float, z: Float) {
        handle.setX(x)
        handle.setY(y)
        handle.setZ(z)
        GameServer.sendTeleport(handle, x, y, z)
    }
}