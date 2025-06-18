package com.asledgehammer.langpack.core

import java.util.*

/**
 * **Language** packages [Locale] as convenient ways to identify and
 * handle language-specific tasks for [LangPack].
 *
 * (Primary identifier is based on Locale objects rather than Strings)
 *
 * @author Jab
 * @property locale The locale to package.
 * @property fallback The fallback Language helps with generalizing
 *     specific Locales sharing the same language.
 */
class Language(val locale: Locale, val fallback: Language? = null) {

  /** The string-form of the locale. */
  val rawLocale = if (locale.country.isNotEmpty()) "${locale.language}_${locale.country}" else locale.language!!

  override fun toString(): String = "Language($rawLocale)"
}
