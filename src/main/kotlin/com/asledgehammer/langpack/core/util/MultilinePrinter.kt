package com.asledgehammer.langpack.core.util

/**
 * **MultilinePrinter** is a utility to print complex objects in a
 * legible way for debugging purposes.
 *
 * @param E The type of object to print.
 */
abstract class MultilinePrinter<E> {

  private var text = ""
  private var prefix = ""
  private var tab = "  "

  /**
   * Prints an element to a string.
   *
   * @param element The element to print.
   * @param tab (Optional) The tab to print for each indentation.
   */
  @JvmOverloads
  fun print(element: E, tab: String = "  "): String {
    this.tab = tab
    onPrint(element)
    val text = text
    reset()
    return text
  }

  /**
   * Appends a raw string without any formatting.
   *
   * @param string The string to append.
   */
  protected fun raw(string: String) {
    text += string
  }

  /**
   * Appends a line.
   *
   * @param string The string to append.
   */
  protected fun line(string: String) {
    text += "$prefix$string\n"
  }

  /** Indents lines by one tab. */
  protected fun tab() {
    prefix += tab
  }

  /** Un-indents lines by one tab. */
  protected fun unTab() {
    val index = prefix.length - tab.length
    prefix = if (index < 1) "" else prefix.substring(0, index)
  }

  /** Resets the tab indentation. */
  protected fun resetTab() {
    prefix = ""
  }

  /** Resets the printer. */
  private fun reset() {
    text = ""
    prefix = ""
    tab = "  "
  }

  /**
   * Printer code is executed here.
   *
   * @param element The element to print.
   */
  protected abstract fun onPrint(element: E)
}
