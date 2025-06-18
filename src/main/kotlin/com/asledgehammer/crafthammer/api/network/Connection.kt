package com.asledgehammer.crafthammer.api.network

/**
 * **Connection** TODO: Document.
 *
 * @author Jab
 */
interface Connection {

  /**
   * The SteamID if SteamMode is enabled, or the IPAddress if otherwise.
   */
  val id: String
  val ownerId: Long
  val accesslevel: Byte
  val accesslevelName: String
  val username: String
  val guid: Long
  val fullyConnected: Boolean
  val timeConnected: Long

  fun sendMessage(name: String, message: String)
  fun disconnect(reason: String? = "Generic")
}
