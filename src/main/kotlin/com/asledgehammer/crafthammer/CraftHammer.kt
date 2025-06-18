@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.asledgehammer.crafthammer

import com.asledgehammer.crafthammer.api.Console
import com.asledgehammer.crafthammer.api.Hammer
import com.asledgehammer.crafthammer.api.command.Commands
import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.Events
import com.asledgehammer.crafthammer.api.event.log.LogListener
import com.asledgehammer.crafthammer.api.permission.Permissions
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.crafthammer.util.component.TextComponent
import com.asledgehammer.craftnail.CraftNail
import com.asledgehammer.langpack.core.LangPack
import java.util.*

/**
 * **CraftHammer** is the base class for all Craftboid operations. All
 * handling of native code for PZ goes through here.
 *
 * @author Jab
 */
object CraftHammer : Hammer {

  override val VERSION = CraftNail.VERSION
  override val BETA_VERSION = CraftNail.BETA_VERSION
  override val IS_BETA = CraftNail.IS_BETA

  override val lang: LangPack = CraftNail.lang

  override var events: Events = CraftEvents()
  override var commands: Commands = CraftCommands()
  override var permissions: Permissions = CraftPermissions()

  override val cfg: YamlFile get() = CraftNail.cfg
  override val console: Console = CraftConsole()
  override val onlinePlayers: Collection<Player> get() = CraftNail.onlinePlayers

  override fun broadcast(name: String, message: String, filter: ((player: Player) -> Boolean)?) {
    val players = if (filter != null) onlinePlayers.filter(filter) else onlinePlayers
    for (player in players) {
      if (!player.online) continue
      player.sendMessage(name, message)
    }
  }

  override fun broadcast(name: String, message: TextComponent, filter: ((player: Player) -> Boolean)?) {
    val players = if (filter != null) onlinePlayers.filter(filter) else onlinePlayers
    for (player in players) {
      if (!player.online) continue
      player.sendMessage(name, message)
    }
  }

  override fun log(list: List<Any?>) = CraftNail.log(list)
  override fun log(vararg objects: Any?) = CraftNail.log(*objects)
  override fun logError(message: String, cause: Throwable?) = CraftNail.logError(message, cause)
  override fun addLogListener(id: UUID, listener: LogListener) = CraftNail.addLogListener(id, listener)
  override fun removeLogListener(id: UUID, listener: LogListener) = CraftNail.removeLogListener(id, listener)
  override fun removeLogListeners(id: UUID) = CraftNail.removeLogListeners(id)
  override fun isPvpEnabled(): Boolean = CraftNail.isPvpEnabled()

  init {
    Hammer.instance = this
  }
}