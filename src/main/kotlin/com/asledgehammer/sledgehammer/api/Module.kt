package com.asledgehammer.sledgehammer.api

import com.asledgehammer.crafthammer.api.LogSupported
import com.asledgehammer.crafthammer.api.command.CommandListener
import com.asledgehammer.crafthammer.api.event.EventListener
import com.asledgehammer.crafthammer.api.event.log.LogListener
import com.asledgehammer.crafthammer.util.cfg.CFGSection
import java.io.File
import java.util.*

interface Module: LogSupported, EventListener, CommandListener {

  val id: UUID
  val directory: File
  val plugin: Plugin
  val properties: Properties
  val loaded: Boolean
  val enabled: Boolean

  /**
   * @param path The path to the File inside the Jar File.
   * @param overwrite The flag to set if the File is to be overwritten,
   *     regardless of it's state.
   */
  fun saveResource(path: String, overwrite: Boolean = false)
  fun saveResourceAs(path: String, filePath: String, overwrite: Boolean = false)
  fun saveResourceAs(path: String, filePath: File, overwrite: Boolean = false)

  fun addEventListener(listener: EventListener)
  fun removeEventListener(listener: EventListener)

  fun addCommandListener(listener: CommandListener)
  fun removeCommandListener(listener: CommandListener)

  fun addLogListener(listener: LogListener)
  fun removeLogListener(listener: LogListener)
  fun removeLogListeners()

  interface Properties {
    val plugin: Plugin.Properties
    val cfg: CFGSection
    val name: String
    val version: String
    val location: String
    val description: String?
  }
}