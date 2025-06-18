@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.asledgehammer.langpack.core.util

/**
 * **MapPrinter** is a multiline printer for Java maps.
 *
 * @author Jab
 */
class MapPrinter<K, V> : MultilinePrinter<Map<K, V>>() {

  override fun onPrint(element: Map<K, V>) {
    recurse(null, element)
  }

  private fun recurse(key: String?, value: Any?) {
    if (value is Map<*, *>) {
      if (key != null) line("$key: {")
      else line("{")
      tab()
      recurseMap(value)
      unTab()
      line("},")
    } else if (value is List<*>) {
      if (key != null) line("$key: [")
      else line("[")
      tab()
      recurseList(value)
      unTab()
      line("],")
    } else {
      if (key != null) line("$key: $value")
      else line("$value,")
    }
  }

  private fun recurseList(list: List<*>) {
    for ((index, item) in list.withIndex()) {
      recurse("$index", item)
    }
  }

  private fun recurseMap(map: Map<*, *>) {
    for ((key, value) in map) {
      if (value != null) recurse(key.toString(), value)
    }
  }
}
