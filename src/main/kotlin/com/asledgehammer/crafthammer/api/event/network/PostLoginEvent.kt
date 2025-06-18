package com.asledgehammer.crafthammer.api.event.network

import com.asledgehammer.crafthammer.api.event.Cancelable
import com.asledgehammer.crafthammer.api.network.Connection

/**
 * **PostLoginEvent** TODO: Document.
 *
 * @author Jab
 */
class PostLoginEvent(connection: Connection) : NetworkEvent(connection), Cancelable {
  override var cancelled: Boolean = false
  override fun toString(): String =
    "PostLoginEvent(async=${async}, timestamp=${timestamp}, handled=${handled}, " +
            "connection=$connection, cancelled=$cancelled)"
}
