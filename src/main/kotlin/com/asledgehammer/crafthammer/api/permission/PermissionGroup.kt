@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.asledgehammer.crafthammer.api.permission

import java.util.*

/**
 * **PermissionGroup** TODO: Document.
 *
 * @author Jab
 */
class PermissionGroup(name: String) : PermissionCollection(name) {

  var parent: PermissionGroup? = null
  val members: List<PermissionUser> get() = _members.values.toList()
  private val _members = HashMap<String, PermissionUser>()

  override fun has(context: String): Boolean {
    var returned = false
    var parentSpecific: Permission? = null
    if (parent != null) {
      parentSpecific = parent!!.get(context)
      returned = parentSpecific.flag
    }
    val permissionSpecific: Permission? = get(context)
    if (permissionSpecific != null) {
      returned = if (parentSpecific != null) {
        if (parentSpecific == permissionSpecific || parentSpecific.isSuper(permissionSpecific)) {
          permissionSpecific.flag
        } else parentSpecific.flag
      } else permissionSpecific.flag
    } else {
      if (allPermissions.isNotEmpty()) {
        var permission: Permission? = null
        for (permissionNodeNext in allPermissions) {
          if (permissionNodeNext.isSub(context)
            && (permission == null || permission.isSub(permissionNodeNext))
          ) {
            permission = permissionNodeNext
          }
        }
        if (permission != null) returned = permission.flag
      }
    }
    return returned
  }

  override fun getAllSub(context: String): List<Permission> {
    val superContext = context.lowercase(Locale.getDefault()).trim()
    val list = ArrayList<Permission>()
    if (parent != null) list.addAll(parent!!.getAllSub(superContext))
    for ((_, permission) in permissions) {
      if (permission.isSub(superContext)) {
        if (list.contains(permission)) {
          list.remove(permission)
          list.add(permission)
        }
      }
    }
    return list
  }

  override fun equals(other: Any?): Boolean = other is PermissionGroup && other.name.equals(this.name, true)
  override fun hashCode(): Int = 31 * name.hashCode()
  override fun toString(): String = "PermissionGroup(name=$name)"

  /** TODO: Document. */
  val allPermissions: List<Permission>
    get() {
      val list = ArrayList<Permission>()
      if (parent != null) list.addAll(parent!!.allPermissions)
      if (list.isEmpty()) return ArrayList(permissions.values)

      for ((_, node) in permissions) {
        for (nodeParent in list) {
          if (nodeParent == node) {
            list.remove(nodeParent)
            list.add(node)
          }
        }
      }
      return list
    }

  /** TODO: Document. */
  fun isChildOf(other: PermissionGroup): Boolean = parent != null && (parent == other || parent!!.isChildOf(other))

  /** TODO: Document. */
  fun hasMember(member: PermissionUser): Boolean = _members.containsKey(member.name.lowercase())

  /** TODO: Document. */
  fun addMember(user: PermissionUser) {
    _members[user.name.lowercase()] = user
    user.group = this
  }

  /** TODO: Document. */
  fun removeMember(user: PermissionUser) {
    _members.remove(user.name.lowercase())
    user.group = null
  }

  /** TODO: Document. */
  fun hasMembers(): Boolean = _members.isNotEmpty()
}
