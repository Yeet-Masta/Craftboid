@file:Suppress("MemberVisibilityCanBePrivate", "unused", "SameParameterValue")
@file:OptIn(ExperimentalUnsignedTypes::class)

package com.asledgehammer.crafthammer.util.color

import com.asledgehammer.crafthammer.util.console.ANSIUtils
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

/**
 * **Color** TODO: Document.
 *
 * @author Jab
 */
class Color(var r: UByte, var g: UByte, var b: UByte, var a: UByte = 255u) {

  fun toChatColor(): String = "<RBG;${r.toFloat() / 255.0f};${g.toFloat() / 255.0F};${b.toFloat() / 255.0F}>"

  fun toAnsiColor(): String = ANSIUtils.toANSIForeground(this)

  companion object {

    val WHITE: Color = fromBytes(255, 255, 255)

    fun fromBytes(r: Int, g: Int, b: Int, a: Int = 255): Color {
      verifyUByte(r, "red")
      verifyUByte(g, "green")
      verifyUByte(b, "blue")
      verifyUByte(a, "alpha")
      return Color(r.toUByte(), g.toUByte(), b.toUByte(), a.toUByte())
    }

    fun fromDecimal(r: Double, g: Double, b: Double, a: Double = 1.0): Color {
      val red = floor(max(0.0, min(1.0, r)).toFloat() * 255.0F).toInt().toUByte()
      val green = floor(max(0.0, min(1.0, g)).toFloat() * 255.0F).toInt().toUByte()
      val blue = floor(max(0.0, min(1.0, b)).toFloat() * 255.0F).toInt().toUByte()
      val alpha = floor(max(0.0, min(1.0, a)).toFloat() * 255.0F).toInt().toUByte()
      return Color(red, green, blue, alpha)
    }

    fun fromAnsiColor(string: String): Color = ANSIUtils.toColorForeground(string)

    fun fromPZColor(string: String): Color {
      // <RGB;r;g;b>
      require(string.length > 10) { "Invalid PZ color: $string" }

      val colors = string.substring(5).replace(">", "").split(";")
      require(colors.size == 3) { "Invalid PZ color: $string" }

      val r = colors[0].toInt().toUByte()
      val g = colors[1].toInt().toUByte()
      val b = colors[2].toInt().toUByte()

      return Color(r, g, b)
    }

    private fun verifyUByte(value: Int, name: String) {
      verifyRange(0, 255, value, name)
    }

    private fun verifyRange(min: Int, max: Int, value: Int, name: String) {
      require(value > min - 1 && value < max + 1) {
        "The value '${name}' is out of range. (min: $min, max: $max, given: $value)"
      }
    }
  }

}