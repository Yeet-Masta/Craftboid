@file:Suppress("unused")

package com.asledgehammer.crafthammer.util.math

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * **MathUtils** TODO: Document.
 *
 * @author Jab
 */
object MathUtils {

  val random = Random()

  @JvmStatic
  fun distanceSquared3i(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Double = sqrt(
    (x2 - x1).toDouble().pow(2)
            + (y2 - y1).toDouble().pow(2)
            + (z2 - z1).toDouble().pow(2)
  )

  @JvmStatic
  fun distanceSquared3(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Double = sqrt(
    (x2 - x1).pow(2)
            + (y2 - y1).pow(2)
            + (z2 - z1).pow(2)
  )

  @JvmStatic
  fun verifyMinMax(min: Int, max: Int) {
    require(min > -1 && min < 256 && max > -1 && max < 256) {
      "Invalid min and max values. Min and max values should be between 0 and 255. Given: (min: $min, max: $max)"
    }

    require(min <= max) {
      "Invalid min and max values. Min is greater than max. Given: (min: $min, max: $max)"
    }
  }

  @JvmStatic
  @JvmOverloads
  fun verifyUByteParam(value: Int, name: String = "param") {
    verifyValueRange(0, 255, value, name)
  }

  @JvmStatic
  @JvmOverloads
  fun verifyValueRange(min: Int, max: Int, value: Int, name: String = "param") {
    require(value > min - 1 && value < max + 1) {
      "The value '${name}' is out of range. (min: $min, max: $max, given: $value)"
    }
  }
}