package com.asledgehammer.crafthammer.api.permission

/**
 * **PermissionSupported** TODO: Document.
 *
 * @author Jab
 */
interface PermissionSupported {
  fun hasPermission(context: String): Boolean
  fun setPermission(context: String, flag: Boolean)
}
