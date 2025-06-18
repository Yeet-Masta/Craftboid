package com.asledgehammer.sledgehammer.api

import java.io.File

interface Plugins {

  val plugins: HashMap<String, Plugin>
  val directory: File

  fun load(directory: File)
  fun enable()
  fun tick()
  fun disable()
  fun unload()
  fun clear()

  fun <T : Plugin?> getPlugin(clazz: Class<out Plugin?>): T?

  companion object {
    var instance: Plugins? = null
  }
}