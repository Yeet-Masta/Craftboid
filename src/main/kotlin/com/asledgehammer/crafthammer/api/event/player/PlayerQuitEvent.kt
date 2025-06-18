package com.asledgehammer.crafthammer.api.event.player

import com.asledgehammer.crafthammer.api.entity.Player

/**
 * **PlayerQuitEvent** TODO: Document.
 *
 * @author Jab
 */
class PlayerQuitEvent(player: Player) : PlayerEvent(player) {
  override fun toString(): String =
    "PlayerQuitEvent(async=${async}, timestamp=${timestamp}, handled=${handled}, " +
            "player=${player})"
}