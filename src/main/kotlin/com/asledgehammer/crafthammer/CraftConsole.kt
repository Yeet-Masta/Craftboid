package com.asledgehammer.crafthammer

import com.asledgehammer.crafthammer.api.Console
import com.asledgehammer.crafthammer.util.component.TextComponent

class CraftConsole : Console {

  override fun sendMessage(name: String?, message: String) {
    if (name != null) println("$name: $message")
    else println(message)
  }

  override fun sendMessage(name: String?, message: TextComponent) {
    if (name != null) println("$name: ${message.format(TextComponent.Format.CONSOLE)}")
    else println(message.format((TextComponent.Format.CONSOLE)))
  }

  override fun hasPermission(context: String): Boolean = true
  override fun setPermission(context: String, flag: Boolean) {}
}