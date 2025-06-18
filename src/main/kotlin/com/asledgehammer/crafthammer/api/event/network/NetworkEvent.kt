package com.asledgehammer.crafthammer.api.event.network

import com.asledgehammer.crafthammer.api.event.Event
import com.asledgehammer.crafthammer.api.network.Connection

/**
 * **NetworkEvent** TODO: Document.
 *
 * @author Jab
 */
abstract class NetworkEvent(val connection: Connection) : Event() {
  override fun toString(): String =
    "NetworkEvent(async=${async}, timestamp=${timestamp}, handled=${handled}, connection=$connection)"
}