@file:Suppress("unused")

package com.asledgehammer.crafthammer.util.color

/**
 * **ColorUtils** provides utilities for when working with color values.
 *
 * @author Jab
 */
object ColorUtils {

  /**
   * Packages unsigned RGB values as a 32-bit Integer.
   *
   * @param red The red channel value. (0-255)
   * @param green The green channel value. (0-255)
   * @param blue The blue channel value. (0-255)
   *
   * @return The packaged 32-bit Integer containing the red, green, and blue values.
   *
   * @throws NullPointerException Thrown if the red, green, or blue values are null.
   * @throws IllegalArgumentException Thrown if the following occurs:
   * - The 'red' value is less than 0 or greater than 255.
   * - The 'green' value is less than 0 or greater than 255.
   * - The 'blue' value is less than 0 or greater than 255.
   */
  fun rgb2int(red: Int, green: Int, blue: Int): Int {
    verifyRGB(red, green, blue)
    var rgb: Int = red
    rgb = (rgb shl 8) + green
    return (rgb shl 8) + blue
  }

  /**
   * Unpacks unsigned RGB values from a 32-bit Integer.
   *
   * @param value The 32-bit Integer to unpack.
   *
   * @return An array of the red, green, and blue values respectively.
   *
   * @throws NullPointerException Thrown if the value is null.
   */
  fun int2rgb(value: Int): Array<Int> {
    val red: Int = value shr 16 and 0xFF
    val green: Int = value shr 8 and 0xFF
    val blue: Int = value and 0xFF

    return arrayOf(red, green, blue)
  }

  @JvmStatic
  fun verifyRGB(r: Int, g: Int, b: Int) {
    require(r > -1 && r < 256 && g > -1 && g < 256 && b > -1 && b < 256) {
      "Invalid color values. RGB values should be between 0 and 255. Given: (r: $r, g: $g, b: $b)"
    }
  }
}