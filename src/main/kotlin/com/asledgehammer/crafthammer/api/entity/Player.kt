package com.asledgehammer.crafthammer.api.entity

import com.asledgehammer.crafthammer.api.command.CommandSender
import com.asledgehammer.crafthammer.api.command.Messagable
import com.asledgehammer.crafthammer.api.network.Connection
import org.joml.Vector3f

/**
 * **Player** TODO: Document.
 *
 * @author Jab
 */
interface Player : Character, Messagable, CommandSender {

  val username: String
  val displayName: String
  val index: Short
  val accessLevel: AccessLevel
  val connection: Connection
  val online: Boolean
  val isStaff: Boolean

  fun disconnect(reason: String? = "Generic")
  fun teleport(location: Vector3f)
  fun teleport(x: Float, y: Float, z: Float)

}