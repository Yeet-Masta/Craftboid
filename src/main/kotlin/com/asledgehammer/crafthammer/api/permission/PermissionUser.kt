package com.asledgehammer.crafthammer.api.permission

/**
 * **PermissionUser** TODO: Document.
 *
 * @author Jab
 * @param name
 * @property group
 */
class PermissionUser(name: String, var group: PermissionGroup? = null) :
  PermissionCollection(name) {

  override fun has(context: String): Boolean {
    val permissionGroup: Permission? = group?.getClosest(context)
    val permissionUser = getClosest(context)
    if (permissionUser != null) {
      return if (permissionGroup != null) {
        if (permissionGroup == permissionUser || permissionUser.isSub(permissionGroup)) permissionUser.flag
        else permissionGroup.flag
      } else permissionUser.flag
    } else if (permissionGroup != null) return permissionGroup.flag
    return false
  }

  override fun toString(): String = "PermissionUser(name=$name)"
}
