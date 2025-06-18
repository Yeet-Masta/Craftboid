package com.asledgehammer.crafthammer.api.permission

import java.io.File

/**
 * **Permissions** TODO: Document.
 *
 * @author Jab
 */
interface Permissions {

  val defaultGroup: PermissionGroup

  fun load()
  fun save()

  fun createGroup(name: String): PermissionGroup
  fun removeGroup(group: PermissionGroup)
  fun removeGroup(name: String)
  fun getGroup(name: String): PermissionGroup
  fun hasGroup(username: String): Boolean

  fun createUser(name: String): PermissionUser
  fun removeUser(user: PermissionUser)
  fun removeUser(name: String)
  fun getUser(name: String): PermissionUser
  fun hasUser(name: String): Boolean
}