@file:Suppress("MemberVisibilityCanBePrivate", "unused", "EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

package com.asledgehammer.crafthammer.util.console

import com.asledgehammer.crafthammer.util.color.Color
import com.asledgehammer.crafthammer.util.math.MathUtils
import org.fusesource.jansi.AnsiConsole
import java.util.*
import kotlin.collections.HashMap


/**
 * **ANSIUtils** provides utilities for when working with escape-codes in the console.
 *
 * @author Jab
 */
object ANSIUtils {

  private const val ESC = '\u001b'
  private var supported = false

  val ANSI_CLEAR_SCREEN: String
  val ANSI_RESET: String
  val ANSI_BOLD: String
  val ANSI_FAINT: String
  val ANSI_ITALIC: String
  val ANSI_UNDERLINE: String
  val ANSI_SLOW_BLINK: String
  val ANSI_RAPID_BLINK: String
  val ANSI_INVERT: String
  val ANSI_HIDE: String
  val ANSI_STRIKETHROUGH: String
  val ANSI_DEFAULT_FONT: String
  val ANSI_ALT_FONT_1: String
  val ANSI_ALT_FONT_2: String
  val ANSI_ALT_FONT_3: String
  val ANSI_ALT_FONT_4: String
  val ANSI_ALT_FONT_5: String
  val ANSI_ALT_FONT_6: String
  val ANSI_ALT_FONT_7: String
  val ANSI_ALT_FONT_8: String
  val ANSI_ALT_FONT_9: String
  val ANSI_GOTHIC_FONT: String
  val ANSI_DOUBLE_UNDERLINE: String
  val ANSI_NORMAL_INTENSITY: String
  val ANSI_REVEAL: String

  val ANSI_BLACK: String
  val ANSI_RED: String
  val ANSI_GREEN: String
  val ANSI_YELLOW: String
  val ANSI_BLUE: String
  val ANSI_PURPLE: String
  val ANSI_CYAN: String
  val ANSI_WHITE: String
  val ANSI_RGB_HEADER: String
  val ANSI_RESET_FOREGROUND: String
  val ANSI_BG_BLACK: String
  val ANSI_BG_RED: String
  val ANSI_BG_GREEN: String
  val ANSI_BG_YELLOW: String
  val ANSI_BG_BLUE: String
  val ANSI_BG_PURPLE: String
  val ANSI_BG_CYAN: String
  val ANSI_BG_WHITE: String
  val ANSI_BG_RGB_HEADER: String
  val ANSI_RESET_BACKGROUND: String

  val ANSI_GRAY: String
  val ANSI_BRIGHT_RED: String
  val ANSI_BRIGHT_GREEN: String
  val ANSI_BRIGHT_YELLOW: String
  val ANSI_BRIGHT_BLUE: String
  val ANSI_BRIGHT_MAGENTA: String
  val ANSI_BRIGHT_CYAN: String
  val ANSI_BRIGHT_WHITE: String
  val ANSI_BG_GRAY: String
  val ANSI_BG_BRIGHT_RED: String
  val ANSI_BG_BRIGHT_GREEN: String
  val ANSI_BG_BRIGHT_YELLOW: String
  val ANSI_BG_BRIGHT_BLUE: String
  val ANSI_BG_BRIGHT_MAGENTA: String
  val ANSI_BG_BRIGHT_CYAN: String
  val ANSI_BG_BRIGHT_WHITE: String
  val ANSI_VALUES: List<String>

  val ansiMapCache = HashMap<Color, String>()
  val ansiBGMapCache = HashMap<Color, String>()
  private val ansiMap = HashMap<Color, String>()
  private val ansiBGMap = HashMap<Color, String>()

  init {

    fun registerFGColor(r: Int, g: Int, b: Int, code: String) {
      ansiMap[Color(r.toUByte(), g.toUByte(), b.toUByte())] = code
    }

    fun registerBGColor(r: Int, g: Int, b: Int, code: String) {
      ansiBGMap[Color(r.toUByte(), g.toUByte(), b.toUByte())] = code
    }

    val windows = System.getProperty("os.name").lowercase(Locale.getDefault()).startsWith("win")
    if (windows) {
      AnsiConsole.systemInstall()
    }

    supported = true

    ANSI_CLEAR_SCREEN = if (supported) "$ESC[2J" else ""
    ANSI_RESET = if (supported) "$ESC[0m" else ""
    ANSI_BOLD = if (supported) "$ESC[1m" else ""
    ANSI_FAINT = if (supported) "$ESC[2m" else ""
    ANSI_ITALIC = if (supported) "$ESC[3m" else ""
    ANSI_UNDERLINE = if (supported) "$ESC[4m" else ""
    ANSI_SLOW_BLINK = if (supported) "$ESC[5m" else ""
    ANSI_RAPID_BLINK = if (supported) "$ESC[6m" else ""
    ANSI_INVERT = if (supported) "$ESC[7m" else ""
    ANSI_HIDE = if (supported) "$ESC[8m" else ""
    ANSI_STRIKETHROUGH = if (supported) "$ESC[9m" else ""
    ANSI_DEFAULT_FONT = if (supported) "$ESC[10m" else ""
    ANSI_ALT_FONT_1 = if (supported) "$ESC[11m" else ""
    ANSI_ALT_FONT_2 = if (supported) "$ESC[12m" else ""
    ANSI_ALT_FONT_3 = if (supported) "$ESC[13m" else ""
    ANSI_ALT_FONT_4 = if (supported) "$ESC[14m" else ""
    ANSI_ALT_FONT_5 = if (supported) "$ESC[15m" else ""
    ANSI_ALT_FONT_6 = if (supported) "$ESC[16m" else ""
    ANSI_ALT_FONT_7 = if (supported) "$ESC[17m" else ""
    ANSI_ALT_FONT_8 = if (supported) "$ESC[18m" else ""
    ANSI_ALT_FONT_9 = if (supported) "$ESC[19m" else ""
    ANSI_GOTHIC_FONT = if (supported) "$ESC[20m" else ""
    ANSI_DOUBLE_UNDERLINE = if (supported) "$ESC[21m" else ""
    ANSI_NORMAL_INTENSITY = if (supported) "$ESC[22m" else ""
    ANSI_REVEAL = if (supported) "$ESC[28m" else ""
    ANSI_BLACK = if (supported) "$ESC[30m" else ""
    ANSI_RED = if (supported) "$ESC[31m" else ""
    ANSI_GREEN = if (supported) "$ESC[32m" else ""
    ANSI_YELLOW = if (supported) "$ESC[33m" else ""
    ANSI_BLUE = if (supported) "$ESC[34m" else ""
    ANSI_PURPLE = if (supported) "$ESC[35m" else ""
    ANSI_CYAN = if (supported) "$ESC[36m" else ""
    ANSI_WHITE = if (supported) "$ESC[37m" else ""
    ANSI_RGB_HEADER = if (supported) "$ESC[38" else ""
    ANSI_RESET_FOREGROUND = if (supported) "$ESC[39;" else ""
    ANSI_BG_BLACK = if (supported) "$ESC[40m" else ""
    ANSI_BG_RED = if (supported) "$ESC[41m" else ""
    ANSI_BG_GREEN = if (supported) "$ESC[42m" else ""
    ANSI_BG_YELLOW = if (supported) "$ESC[43m" else ""
    ANSI_BG_BLUE = if (supported) "$ESC[44m" else ""
    ANSI_BG_PURPLE = if (supported) "$ESC[45m" else ""
    ANSI_BG_CYAN = if (supported) "$ESC[46m" else ""
    ANSI_BG_WHITE = if (supported) "$ESC[47m" else ""
    ANSI_BG_RGB_HEADER = if (supported) "$ESC[48" else ""
    ANSI_RESET_BACKGROUND = if (supported) "$ESC[49m" else ""
    ANSI_GRAY = if (supported) "$ESC[90m" else ""
    ANSI_BRIGHT_RED = if (supported) "$ESC[91m" else ""
    ANSI_BRIGHT_GREEN = if (supported) "$ESC[92m" else ""
    ANSI_BRIGHT_YELLOW = if (supported) "$ESC[93m" else ""
    ANSI_BRIGHT_BLUE = if (supported) "$ESC[94m" else ""
    ANSI_BRIGHT_MAGENTA = if (supported) "$ESC[95m" else ""
    ANSI_BRIGHT_CYAN = if (supported) "$ESC[96m" else ""
    ANSI_BRIGHT_WHITE = if (supported) "$ESC[97m" else ""
    ANSI_BG_GRAY = if (supported) "$ESC[100m" else ""
    ANSI_BG_BRIGHT_RED = if (supported) "$ESC[101m" else ""
    ANSI_BG_BRIGHT_GREEN = if (supported) "$ESC[102m" else ""
    ANSI_BG_BRIGHT_YELLOW = if (supported) "$ESC[103m" else ""
    ANSI_BG_BRIGHT_BLUE = if (supported) "$ESC[104m" else ""
    ANSI_BG_BRIGHT_MAGENTA = if (supported) "$ESC[105m" else ""
    ANSI_BG_BRIGHT_CYAN = if (supported) "$ESC[106m" else ""
    ANSI_BG_BRIGHT_WHITE = if (supported) "$ESC[107m" else ""

    ANSI_VALUES = listOf(
      ANSI_RESET,
      ANSI_BOLD,
      ANSI_FAINT,
      ANSI_ITALIC,
      ANSI_UNDERLINE,
      ANSI_SLOW_BLINK,
      ANSI_RAPID_BLINK,
      ANSI_INVERT,
      ANSI_HIDE,
      ANSI_STRIKETHROUGH,
      ANSI_DEFAULT_FONT,
      ANSI_ALT_FONT_1,
      ANSI_ALT_FONT_2,
      ANSI_ALT_FONT_3,
      ANSI_ALT_FONT_4,
      ANSI_ALT_FONT_5,
      ANSI_ALT_FONT_6,
      ANSI_ALT_FONT_7,
      ANSI_ALT_FONT_8,
      ANSI_ALT_FONT_9,
      ANSI_GOTHIC_FONT,
      ANSI_DOUBLE_UNDERLINE,
      ANSI_NORMAL_INTENSITY,
      ANSI_REVEAL,
      ANSI_BLACK,
      ANSI_RED,
      ANSI_GREEN,
      ANSI_YELLOW,
      ANSI_BLUE,
      ANSI_PURPLE,
      ANSI_CYAN,
      ANSI_WHITE,
      ANSI_RESET_FOREGROUND,
      ANSI_BG_BLACK,
      ANSI_BG_RED,
      ANSI_BG_GREEN,
      ANSI_BG_YELLOW,
      ANSI_BG_BLUE,
      ANSI_BG_PURPLE,
      ANSI_BG_CYAN,
      ANSI_BG_WHITE,
      ANSI_RESET_BACKGROUND,
      ANSI_GRAY,
      ANSI_BRIGHT_RED,
      ANSI_BRIGHT_GREEN,
      ANSI_BRIGHT_YELLOW,
      ANSI_BRIGHT_BLUE,
      ANSI_BRIGHT_MAGENTA,
      ANSI_BRIGHT_CYAN,
      ANSI_BRIGHT_WHITE,
      ANSI_BG_GRAY,
      ANSI_BG_BRIGHT_RED,
      ANSI_BG_BRIGHT_GREEN,
      ANSI_BG_BRIGHT_YELLOW,
      ANSI_BG_BRIGHT_BLUE,
      ANSI_BG_BRIGHT_MAGENTA,
      ANSI_BG_BRIGHT_CYAN,
      ANSI_BG_BRIGHT_WHITE
    )

    // Regular Colors
    registerFGColor(0, 0, 0, ANSI_BLACK)
    registerFGColor(170, 0, 0, ANSI_RED)
    registerFGColor(0, 170, 0, ANSI_GREEN)
    registerFGColor(170, 85, 0, ANSI_YELLOW)
    registerFGColor(0, 0, 170, ANSI_BLUE)
    registerFGColor(170, 0, 170, ANSI_PURPLE)
    registerFGColor(0, 170, 170, ANSI_CYAN)
    registerFGColor(170, 170, 170, ANSI_WHITE)

    // Bright Colors
    registerFGColor(85, 85, 85, ANSI_GRAY)
    registerFGColor(255, 85, 85, ANSI_BRIGHT_RED)
    registerFGColor(85, 255, 85, ANSI_BRIGHT_GREEN)
    registerFGColor(255, 255, 85, ANSI_BRIGHT_YELLOW)
    registerFGColor(85, 85, 255, ANSI_BRIGHT_BLUE)
    registerFGColor(255, 85, 255, ANSI_BRIGHT_MAGENTA)
    registerFGColor(85, 255, 255, ANSI_BRIGHT_CYAN)
    registerFGColor(255, 255, 255, ANSI_BRIGHT_WHITE)


    // Regular Colors
    registerBGColor(0, 0, 0, ANSI_BG_BLACK)
    registerBGColor(170, 0, 0, ANSI_BG_RED)
    registerBGColor(0, 170, 0, ANSI_BG_GREEN)
    registerBGColor(170, 85, 0, ANSI_BG_YELLOW)
    registerBGColor(0, 0, 170, ANSI_BG_BLUE)
    registerBGColor(170, 0, 170, ANSI_BG_PURPLE)
    registerBGColor(0, 170, 170, ANSI_BG_CYAN)
    registerBGColor(170, 170, 170, ANSI_BG_WHITE)

    // Bright Colors
    registerBGColor(85, 85, 85, ANSI_BG_GRAY)
    registerBGColor(255, 85, 85, ANSI_BG_BRIGHT_RED)
    registerBGColor(85, 255, 85, ANSI_BG_BRIGHT_GREEN)
    registerBGColor(255, 255, 85, ANSI_BG_BRIGHT_YELLOW)
    registerBGColor(85, 85, 255, ANSI_BG_BRIGHT_BLUE)
    registerBGColor(255, 85, 255, ANSI_BG_BRIGHT_MAGENTA)
    registerBGColor(85, 255, 255, ANSI_BG_BRIGHT_CYAN)
    registerBGColor(255, 255, 255, ANSI_BG_BRIGHT_WHITE)

  }

  @JvmStatic
  fun strip(string: String): String {
    if (!supported) return string

    var stripped = string

    // Remove all constants.
    for (value in ANSI_VALUES) {
      stripped = stripped.replace(value, "")
    }

    // Remove custom foreground colors.
    if (ANSI_RGB_HEADER.isNotEmpty()) {
      while (stripped.contains(ANSI_RGB_HEADER)) {
        val indexStart = stripped.indexOf(ANSI_RGB_HEADER)
        var indexStop = indexStart
        while (true) {
          if (indexStop >= stripped.length) break
          if (stripped[indexStop] == 'm') break
          indexStop++
        }
        stripped = stripped.removeRange(IntRange(indexStart, indexStop))
      }
    }

    // Remove custom background colors.
    if (ANSI_BG_RGB_HEADER.isNotEmpty()) {
      while (stripped.contains(ANSI_BG_RGB_HEADER)) {
        val indexStart = stripped.indexOf(ANSI_BG_RGB_HEADER)
        var indexStop = indexStart
        while (true) {
          if (indexStop >= stripped.length) break
          if (stripped[indexStop] == 'm') break
          indexStop++
        }
        stripped = stripped.removeRange(IntRange(indexStart, indexStop))
      }
    }

    return stripped
  }

  @JvmStatic
  @JvmOverloads
  fun wrapRGB8(string: String, value: Int, reset: Boolean = true): String {
    if (!supported) return string

    MathUtils.verifyUByteParam(value)
    if (string.isEmpty()) return string
    if (reset) return "$ANSI_RESET${ansiRGB8(value)}$string$ANSI_RESET"
    return "${ansiRGB8(value)}$string"
  }

  @JvmStatic
  @JvmOverloads
  fun grayScale8(string: String, value: Int, reset: Boolean = true): String {
    if (!supported) return string

    MathUtils.verifyUByteParam(value)
    if (string.isEmpty()) return string
    if (reset) return "$ANSI_RESET${ansiRGB8(value)}$string$ANSI_RESET"
    return "${ansiRGB8(value)}$string"
  }

  @JvmStatic
  fun grayScale8(string: String, from: Char, to: Char, value: Int): String {
    if (!supported) return string

    MathUtils.verifyUByteParam(value)
    if (string.isEmpty()) return string
    if (!string.contains(to)) return string
    val color = ansiRGB8(value)
    return string.replace("$from", "$color$to")
  }

  @JvmStatic
  fun grayScale8Range(string: String, from: Char, to: Char, min: Int, max: Int): String {
    if (!supported) return string.replace("$from", "$to")

    MathUtils.verifyMinMax(min, max)

    if (string.isEmpty()) return string
    if (!string.contains(to)) return string

    var transformed = ""
    for (index in string.indices) {
      val char = string[index]
      if (char == from) {
        val value = min + MathUtils.random.nextInt(max - min)
        val color = ansiRGB8(value)
        transformed += "$color$to"
      } else {
        transformed += char
      }
    }

    return transformed
  }

  @JvmStatic
  fun toANSIForeground(color: Color): String {

    if (ansiMapCache.containsKey(color)) return ansiMapCache[color]!!

    val r = color.r.toInt()
    val g = color.g.toInt()
    val b = color.b.toInt()

    if (r == 255 && g == 255 && b == 255) return ANSI_BRIGHT_WHITE
    else if (r == 0 && g == 0 && b == 0) return ANSI_BLACK

    var minDistance = Double.MAX_VALUE
    var value = ANSI_WHITE
    for (next in ansiMap.keys) {
      val nr = next.r.toInt()
      val ng = next.g.toInt()
      val nb = next.b.toInt()
      val nextDistance = MathUtils.distanceSquared3i(nr, r, ng, g, nb, b)
      if (nextDistance < minDistance) {
        minDistance = nextDistance
        value = ansiMap[next]!!
      }
    }

    return value
  }

  @JvmStatic
  fun toColorForeground(string: String): Color {
    for ((key, value) in ansiMap) {
      if (value.equals(string, true)) return key
    }
    throw IllegalArgumentException("Unknown ANSI foreground color: $string")
  }

  @JvmStatic
  fun toANSIBackground(color: Color): String {

    if (ansiBGMapCache.containsKey(color)) return ansiBGMapCache[color]!!

    val r = color.r.toInt()
    val g = color.g.toInt()
    val b = color.b.toInt()

    if (r == 255 && g == 255 && b == 255) return ANSI_BG_BRIGHT_WHITE
    else if (r == 0 && g == 0 && b == 0) return ANSI_BG_BLACK

    var minDistance = Double.MAX_VALUE
    var value = ANSI_BG_WHITE
    for (next in ansiBGMap.keys) {
      val nr = next.r.toInt()
      val ng = next.g.toInt()
      val nb = next.b.toInt()
      val nextDistance = MathUtils.distanceSquared3i(nr, r, ng, g, nb, b)
      if (nextDistance < minDistance) {
        minDistance = nextDistance
        value = ansiMap[next]!!
      }
    }

    return value
  }

  @JvmStatic
  fun toColorBackground(string: String): Color {
    for ((key, value) in ansiBGMap) {
      if (value.equals(string, true)) return key
    }
    throw IllegalArgumentException("Unknown ANSI background color: $string")
  }

  @JvmStatic
  fun ansiRGB8(value: Int): String {
    if (!supported) return ""
    MathUtils.verifyUByteParam(value)
    return "$ANSI_RGB_HEADER;5;${value}m"
  }

  @JvmStatic
  fun ansiBGRGB8(value: Int): String {
    if (!supported) return ""
    MathUtils.verifyUByteParam(value)
    return "$ANSI_BG_RGB_HEADER;5;${value}m"
  }

  @JvmStatic
  fun clearScreen() = ANSI_CLEAR_SCREEN

  @JvmStatic
  fun reset() = ANSI_RESET

  @JvmStatic
  fun bold() = ANSI_BOLD

  @JvmStatic
  fun faint() = ANSI_FAINT

  @JvmStatic
  fun italic() = ANSI_ITALIC

  @JvmStatic
  fun underline() = ANSI_UNDERLINE

  @JvmStatic
  fun slowBlink() = ANSI_SLOW_BLINK

  @JvmStatic
  fun fastBlink() = ANSI_RAPID_BLINK

  @JvmStatic
  fun invert() = ANSI_INVERT

  @JvmStatic
  fun hide() = ANSI_HIDE

  @JvmStatic
  fun strikeThrough() = ANSI_STRIKETHROUGH

  @JvmStatic
  fun defaultFont() = ANSI_DEFAULT_FONT

  @JvmStatic
  fun altFont1() = ANSI_ALT_FONT_1

  @JvmStatic
  fun altFont2() = ANSI_ALT_FONT_2

  @JvmStatic
  fun altFont3() = ANSI_ALT_FONT_3

  @JvmStatic
  fun altFont4() = ANSI_ALT_FONT_4

  @JvmStatic
  fun altFont5() = ANSI_ALT_FONT_5

  @JvmStatic
  fun altFont6() = ANSI_ALT_FONT_6

  @JvmStatic
  fun altFont7() = ANSI_ALT_FONT_7

  @JvmStatic
  fun altFont8() = ANSI_ALT_FONT_8

  @JvmStatic
  fun altFont9() = ANSI_ALT_FONT_9

  @JvmStatic
  fun gothicFont() = ANSI_GOTHIC_FONT

  @JvmStatic
  fun doubleUnderline() = ANSI_DOUBLE_UNDERLINE

  @JvmStatic
  fun normalIntensity() = ANSI_NORMAL_INTENSITY

  @JvmStatic
  fun reveal() = ANSI_REVEAL

  @JvmStatic
  fun black() = ANSI_BLACK

  @JvmStatic
  fun red() = ANSI_RED

  @JvmStatic
  fun green() = ANSI_GREEN

  @JvmStatic
  fun yellow() = ANSI_YELLOW

  @JvmStatic
  fun blue() = ANSI_BLUE

  @JvmStatic
  fun purple() = ANSI_PURPLE

  @JvmStatic
  fun cyan() = ANSI_CYAN

  @JvmStatic
  fun white() = ANSI_WHITE

  @JvmStatic
  fun resetForeground() = ANSI_RESET_FOREGROUND

  @JvmStatic
  fun blackBackground() = ANSI_BG_BLACK

  @JvmStatic
  fun redBackground() = ANSI_BG_RED

  @JvmStatic
  fun greenBackground() = ANSI_BG_GREEN

  @JvmStatic
  fun yellowBackground() = ANSI_BG_YELLOW

  @JvmStatic
  fun blueBackground() = ANSI_BG_BLUE

  @JvmStatic
  fun purpleBackground() = ANSI_BG_PURPLE

  @JvmStatic
  fun cyanBackground() = ANSI_BG_CYAN

  @JvmStatic
  fun whiteBackground() = ANSI_BG_WHITE

  @JvmStatic
  fun resetBackground() = ANSI_RESET_BACKGROUND

  @JvmStatic
  fun gray() = ANSI_GRAY

  @JvmStatic
  fun brightRed() = ANSI_BRIGHT_RED

  @JvmStatic
  fun brightGreen() = ANSI_BRIGHT_GREEN

  @JvmStatic
  fun brightYellow() = ANSI_BRIGHT_YELLOW

  @JvmStatic
  fun brightBlue() = ANSI_BRIGHT_BLUE

  @JvmStatic
  fun magenta() = ANSI_BRIGHT_MAGENTA

  @JvmStatic
  fun brightCyan() = ANSI_BRIGHT_CYAN

  @JvmStatic
  fun brightWhite() = ANSI_BRIGHT_WHITE

  @JvmStatic
  fun brightGrayBackground() = ANSI_BG_GRAY

  @JvmStatic
  fun brightRedBackground() = ANSI_BG_BRIGHT_RED

  @JvmStatic
  fun brightGreenBackground() = ANSI_BG_BRIGHT_GREEN

  @JvmStatic
  fun brightYellowBackground() = ANSI_BG_BRIGHT_YELLOW

  @JvmStatic
  fun brightBlueBackground() = ANSI_BG_BRIGHT_BLUE

  @JvmStatic
  fun magentaBackground() = ANSI_BG_BRIGHT_MAGENTA

  @JvmStatic
  fun brightCyanBackground() = ANSI_BG_BRIGHT_CYAN

  @JvmStatic
  fun brightWhiteBackground() = ANSI_BG_BRIGHT_WHITE

  @JvmStatic
  fun isSupported() = supported
}