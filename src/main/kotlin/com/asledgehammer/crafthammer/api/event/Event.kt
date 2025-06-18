package com.asledgehammer.crafthammer.api.event

/**
 * **Event** TODO: Document.
 *
 * @author Jab
 */
abstract class Event(val async: Boolean = false) {
  /** The timestamp when the event is created. */
  val timestamp = System.currentTimeMillis()

  /** If the event is handled by a listener. */
  var handled = false

  override fun toString(): String = "Event(timestamp=$timestamp, handled=$handled)"
}