@file:Suppress("MemberVisibilityCanBePrivate")

package com.asledgehammer.crafthammer.util.component

import com.asledgehammer.crafthammer.util.color.Color
import com.asledgehammer.crafthammer.util.console.ANSIUtils

/**
 * **TextComponent** TODO: Document
 *
 * @property text
 * @property color
 * @property bold
 * @property italic
 *
 * @author Jab
 */
class TextComponent @JvmOverloads constructor(
  var text: String = "",
  var color: Color = Color.WHITE,
  var bold: Boolean = false,
  var italic: Boolean = false,
) : Component<TextComponent>() {

  /**
   * Formats the text component to a [Format].
   *
   * @param format The type to format.
   *
   * @return Returns the formatted text-component as a string.
   */
  fun format(format: Format): String {

    val builder: StringBuilder = StringBuilder()

    when (format) {
      Format.CHAT -> {
        builder.append(color.toChatColor()).append(' ')
        if (bold) builder.append("<bold> ")
        if (italic) builder.append("<italic> ")
      }
      Format.CONSOLE -> {
        builder.append(color.toAnsiColor())
        if (bold) builder.append(ANSIUtils.ANSI_BOLD)
        if (italic) builder.append(ANSIUtils.ANSI_ITALIC)
      }
      Format.PLAIN -> {}
    }

    if (text.isNotEmpty()) builder.append(text)
    if (_children.isNotEmpty()) _children.forEach { builder.append(it.format(format)) }

    return builder.toString()
  }

  /**
   * **Format** aids in telling what format to format text-components with [TextComponent.format].
   *
   * @author Jab
   */
  enum class Format {
    CHAT,
    CONSOLE,
    PLAIN;
  }
}