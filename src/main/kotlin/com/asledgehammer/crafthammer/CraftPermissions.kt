@file:Suppress("unused")

package com.asledgehammer.crafthammer

import com.asledgehammer.crafthammer.api.permission.*
import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.craftnail.CraftNail
import java.io.File

/**
 * **CraftPermissions** TODO: Document.
 *
 * @author Jab
 */
class CraftPermissions : Permissions {

  private val groupsByName = HashMap<String, PermissionGroup>()
  private val usersByName = HashMap<String, PermissionUser>()
  override val defaultGroup: PermissionGroup

  init {
    defaultGroup = createGroup("default")
    load()
  }

  override fun load() {

    if (!file.exists()) {
      save()
      return
    }

    val config = YamlFile(file)
    config.read()

    if (config.isSection("users")) loadUsers(config.getSection("users"))

    val groupParentIds = HashMap<String, String>()
    if (config.isSection("groups")) loadGroups(config.getSection("groups"), groupParentIds)
    if (groupsByName.isNotEmpty() && groupParentIds.isNotEmpty()) pairGroups(groupParentIds)
  }

  override fun save() {
    val config = YamlFile(file)
    if (file.exists()) config.read()
    saveGroups(config.createSection("groups"))
    saveUsers(config.createSection("users"))
  }

  override fun createGroup(name: String): PermissionGroup {
    val nameFormatted = format(name)
    require(groupsByName[nameFormatted] == null) { "Permission group already exists: $name" }
    val group = PermissionGroup(name)
    groupsByName[nameFormatted] = group
    return group
  }

  override fun getGroup(name: String): PermissionGroup {
    val nameFormatted = format(name)
    val group = groupsByName[nameFormatted]
    requireNotNull(group) { "Permission group not found: $name" }
    return group
  }

  override fun removeGroup(group: PermissionGroup) {
    groupsByName.remove(format(group.name))
  }

  override fun removeGroup(name: String) {
    val formattedName = format(name)
    val group = groupsByName.remove(formattedName)
    requireNotNull(group) { "Permission group not found: $name" }
    removeGroup(group)
  }

  override fun hasGroup(username: String): Boolean = groupsByName[username] != null

  override fun createUser(name: String): PermissionUser {
    val nameFormatted = format(name)
    require(usersByName[nameFormatted] == null) { "Permission user already exists: $name" }
    val user = PermissionUser(name, null)
    usersByName[nameFormatted] = user

    return user
  }

  override fun getUser(name: String): PermissionUser {
    val nameFormatted = format(name)
    val user = usersByName[nameFormatted]
    requireNotNull(user) { "Permission user not found: $name" }
    return user
  }

  override fun hasUser(name: String): Boolean = usersByName[format(name)] != null

  override fun removeUser(user: PermissionUser) {
    usersByName.remove(format(user.name))
  }

  override fun removeUser(name: String) {
    val nameFormatted = format(name)
    val user = usersByName.remove(nameFormatted)
    requireNotNull(user) { "Permission user not found: $name" }
    removeUser(user)
  }

  private fun loadPermissions(cfg: CFGSection): List<Permission> {
    val list = ArrayList<Permission>()
    val permissions = cfg.getSection("permissions")
    for (key in permissions.allKeys) {
      if (!key.startsWith(CFGSection.SEPARATOR)) {
        CraftHammer.logError("Invalid permission context syntax: $key")
        continue
      }
      if (!permissions.isBoolean(key)) {
        CraftHammer.logError("Invalid permission flag: ${permissions.get(key)}")
        continue
      }
      list.add(Permission(key, permissions.getBoolean(key)))
    }
    return list
  }

  private fun loadUsers(cfg: CFGSection): List<PermissionUser> {
    val list = ArrayList<PermissionUser>()
    for ((_, cfgUser) in cfg.sections) {
      require(cfgUser.contains("name")) { "Permission user does not have a name." }
      require(cfgUser.isString("name")) {
        "Permission user does not have a valid name. (${cfgUser.get("name")})"
      }
      val name = cfgUser.getString("name")
      val user = PermissionUser(name)
      if (cfgUser.isSection("permissions")) {
        val permissions = loadPermissions(cfg.getSection("permissions"))
        user.set(permissions)
      }
      usersByName[format(user.name)] = user
      list.add(user)
    }
    return list
  }

  private fun loadGroups(cfg: CFGSection, groupParentIds: HashMap<String, String>): List<PermissionGroup> {
    val list = ArrayList<PermissionGroup>()
    for ((_, cfgGroup) in cfg.sections) {
      require(cfgGroup.contains("name")) { "Permission group does not have a name." }
      require(cfgGroup.isString("name")) {
        "Permission group does not have a valid name. (${cfgGroup.get("name")})"
      }
      val groupName = cfgGroup.getString("name")
      val group = if (groupName.equals("default", true)) {
        defaultGroup
      } else {
        PermissionGroup(groupName)
      }
      if (cfgGroup.isSection("permissions")) {
        val permissions = loadPermissions(cfg.getSection("permissions"))
        group.set(permissions)
      }

      // No one should belong to the default group, and the default group doesn't have a parent.
      if (group != defaultGroup) {
        if (cfgGroup.isList("members")) {
          for (memberName in cfgGroup.getStringList("members")) {
            val user = usersByName[format(memberName)]
            if (user == null) {
              CraftHammer.logError("User does not exist: (group: ${group.name}, name: $memberName)")
              continue
            }
            group.addMember(user)
          }
        }
        if (cfgGroup.isString("parent")) {
          val parentName = cfgGroup.getString("parent")
          groupParentIds[format(groupName)] = format(parentName)
        }
      }

      groupsByName[group.name] = group
      list.add(group)
    }

    return list
  }

  private fun pairGroups(groupParentIds: HashMap<String, String>) {
    for ((groupName, parentName) in groupParentIds) {
      val group = groupsByName[format(groupName)]!!
      val parent = groupsByName[format(parentName)]
      if (parent == null) {
        System.err.println("Parent for group \"$groupName\" doesn't exist: $parentName")
        continue
      }
      group.parent = parent
    }
  }

  private fun savePermissions(cfg: CFGSection, collection: PermissionCollection) {
    for ((_, permission) in collection.permissions) {
      cfg.set(permission.context, permission.flag)
    }
  }

  private fun saveGroup(cfg: CFGSection, group: PermissionGroup) {
    cfg.set("name", group.name)
    if (group.parent != null) cfg.set("parent", format(group.parent!!.name))
    if (group.hasMembers()) {
      val list = ArrayList<String>()
      for (member in group.members) {
        list.add(format(member.name))
      }
      cfg.set("members", list)
    }
    savePermissions(cfg.createSection("permissions"), group)
  }

  private fun saveUser(cfg: CFGSection, user: PermissionUser) {
    cfg.set("name", user.name)
    savePermissions(cfg.createSection("permissions"), user)
  }

  private fun saveGroups(cfg: CFGSection) {
    for ((_, group) in groupsByName) saveGroup(cfg.createSection(group.name), group)
  }

  private fun saveUsers(cfg: CFGSection) {
    for ((_, user) in usersByName) saveUser(cfg.createSection(format(user.name)), user)
  }

  companion object {

    val file = File(CraftNail.dirCacheServer, "permissions.yml")

    fun format(string: String): String {
      return string.replace('-', '_').replace(' ', '_').lowercase().trim()
    }
  }
}
