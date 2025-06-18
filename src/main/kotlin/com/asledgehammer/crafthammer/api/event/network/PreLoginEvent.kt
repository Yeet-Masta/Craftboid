package com.asledgehammer.crafthammer.api.event.network

import com.asledgehammer.crafthammer.api.event.Cancelable
import com.asledgehammer.crafthammer.api.network.Connection

/**
 * **PreLoginEvent** TODO: Document.
 *
 * @author Jab
 */
class PreLoginEvent(connection: Connection) : NetworkEvent(connection), Cancelable {

  override var cancelled: Boolean = false
  var kickMessage: String = ""

  fun reject(message: String) {
    cancelled = true
    kickMessage = message
  }

  override fun toString(): String =
    "PreLoginEvent(async=${async}, timestamp=${timestamp}, handled=${handled}, " +
            "connection=${connection}, cancelled=$cancelled, kickMessage='$kickMessage')"
}