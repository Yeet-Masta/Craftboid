package com.asledgehammer.crafthammer.api.event.player

import com.asledgehammer.crafthammer.api.entity.Player
import com.asledgehammer.crafthammer.api.event.Event

/**
 * **PlayerEvent** TODO: Document.
 *
 * @author Jab
 */
abstract class PlayerEvent(val player: Player) : Event() {
  override fun toString(): String =
    "PlayerEvent(async=${async}, timestamp=${timestamp}, handled=${handled}, " +
            "player=${player})"
}