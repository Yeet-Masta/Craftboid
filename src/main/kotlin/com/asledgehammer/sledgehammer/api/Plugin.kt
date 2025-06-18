package com.asledgehammer.sledgehammer.api

import com.asledgehammer.crafthammer.util.cfg.CFGSection
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.jar.JarFile

interface Plugin {

  val id: UUID
  val jarFile: JarFile
  val modules: HashMap<String, Module>
  val directory: File
  val properties: Properties

  fun getModule(name: String): Module?
  fun <T : Module?> getModule(clazz: Class<out Module?>): T?
  fun saveResource(path: String)
  fun saveResourceAs(path: String, dstPath: String)
  fun saveResourceAs(path: String, dst: File)
  fun setLoadClasses(flag: Boolean)
  fun getResource(path: String): InputStream?

  interface Properties {
    val modules: HashMap<String, Module.Properties>
    val cfg: CFGSection
    val name: String
    val version: String
    val description: String?
  }
}