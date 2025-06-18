package com.asledgehammer.crafthammer.api.event.player

import com.asledgehammer.crafthammer.api.entity.Player

/**
 * **PlayerJoinEvent** TODO: Document.
 *
 * @author Jab
 */
class PlayerJoinEvent(player: Player) : PlayerEvent(player) {
  override fun toString(): String =
    "PlayerJoinEvent(async=${async}, timestamp=${timestamp}, handled=${handled}, " +
            "player=${player})"

}