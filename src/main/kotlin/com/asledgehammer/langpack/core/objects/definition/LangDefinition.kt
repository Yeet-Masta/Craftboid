@file:Suppress("MemberVisibilityCanBePrivate")

package com.asledgehammer.langpack.core.objects.definition

import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.objects.LangGroup
import com.asledgehammer.langpack.core.objects.formatter.FieldFormatter
import com.asledgehammer.langpack.core.util.StringUtil

/**
 * **LangDefinition** wraps and handles definitions of objects stored in
 * [LangGroup].
 *
 * @property pack The pack the definition belongs to.
 * @property parent The parent group the definition belongs to.
 * @property value The value of the definition.
 * @param E The value's type.
 */
abstract class LangDefinition<E>(val pack: LangPack, val parent: LangGroup?, val raw: E) {

  /** The value of the definition. */
  var value: E = raw

  /** If true, the definition is already walked. */
  var walked: Boolean = false

  /**
   * Walks the definition. Checks [needsWalk] to make sure that
   * non-walkable definitions aren't processed.
   */
  fun walk() {
    if (!walked) {
      if (needsWalk(pack.formatter)) value = onWalk()
      walked = true
    }
  }

  /**
   * Resets the definition's walk transformation. Sets the value [raw]
   * to [value].
   */
  fun unWalk() {
    walked = false
    value = raw
  }

  /**
   * Walks the value for the definition. This allows for post-load
   * transformations of the value.
   *
   * @return Returns the transformed value.
   */
  abstract fun onWalk(): E

  /**
   * @return Returns true if the definition determines that the raw
   *     value needs to walk.
   */
  abstract fun needsWalk(formatter: FieldFormatter): Boolean

  /**
   * Walks a list of strings.
   *
   * @param list The list to walk.
   * @return Returns a list of transformed strings.
   */
  fun walk(list: List<String>): List<String> {
    val walkedList = ArrayList<String>()
    for (string in list) walkedList.add(walk(string))
    return walkedList
  }

  /**
   * Walks a string.
   *
   * @param string The string to walk.
   * @return Returns the transformed string.
   */
  fun walk(string: String): String {
    if (pack.debug) println("Walking ($string)..")

    var value = string
    val fields = pack.formatter.getFields(value)
    val language = parent?.language ?: pack.defaultLang
    val walkedDefinitions = ArrayList<String>()

    for (field in fields) {

      val context = if (field.packageScope) null else parent

      if (!walkedDefinitions.contains(field.raw)) {
        val def = pack.resolve(field.name, language, context)
        if (def != null && !def.walked) def.walk()
        walkedDefinitions.add(field.raw)
      }

      if (field.resolve) {
        // If the field cannot resolve, set the placeholder.
        if (!walkedDefinitions.contains(field.raw)) {
          if (pack.debug) {
            println("Failed to locate resolve field: \"$field\". " +
                    "Using placeholder instead: \"$field.placeholder\".")
          }
          value = value.replace(field.raw, field.placeholder)
          continue
        }

        val resolved = pack.resolve(field.name, language, context)
        val result = if (resolved != null) StringUtil.toAString(resolved.value!!) else field.placeholder

        if (pack.debug) println("""Replacing resolve field "$field" with: "$result".""")
        value = value.replace(field.raw, result)
      }
    }

    if (pack.debug) println("value: $value")
    return value
  }
}
