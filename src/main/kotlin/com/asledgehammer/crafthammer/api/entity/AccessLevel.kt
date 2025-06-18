package com.asledgehammer.crafthammer.api.entity

import java.util.*

/**
 * **AccessLevel** TODO: Document.
 *
 * @author Jab
 */
enum class AccessLevel {
  NONE,
  GM,
  OBSERVER,
  MODERATOR,
  ADMIN;

  companion object {
    fun get(name: String): AccessLevel {
      val nameUpper = name.uppercase(Locale.getDefault()).trim()
      for (next in values()) if (nameUpper.equals(next.name)) return next
      return NONE
    }
  }
}