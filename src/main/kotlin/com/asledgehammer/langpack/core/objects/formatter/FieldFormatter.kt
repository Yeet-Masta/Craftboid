package com.asledgehammer.langpack.core.objects.formatter

import com.asledgehammer.langpack.core.objects.FieldProperties
import com.asledgehammer.langpack.core.util.StringUtil

/**
 * **FieldFormatter** implements the following: field detection, field
 * parsing, and field formatting. This is used to sanitize the core code
 * from hard-coded interpretation and processing of fields.
 *
 * @author Jab
 */
interface FieldFormatter {

  /**
   * Parses a string into fields as metadata.
   *
   * @param string The string to parse.
   * @return Returns a list of the fields as metadata.
   */
  fun getFields(string: String): List<FieldProperties>

  /**
   * Parses a string into a list of raw fields.
   *
   * @param string The string to parse.
   * @return Returns a list of the fields as strings.
   */
  fun getRawFields(string: String): List<String>

  /**
   * Parses a field as metadata.
   *
   * @param field The field to process.
   * @return Returns the parsed field as metadata.
   */
  fun getProperties(field: String): FieldProperties

  /**
   * Parses a string to check for count of fields.
   *
   * @param string The string to parse.
   * @return Returns thr amount of fields in the string.
   */
  fun getFieldCount(string: String): Int = getRawFields(string).size

  /**
   * Parses a field to get the placeholder.
   *
   * @param field The field to parse.
   * @return Returns the placeholder for the field.
   */
  fun getPlaceholder(field: String): String

  /**
   * Strips the field to return the id.
   *
   * @param field The field to process.
   * @return Returns the processed field.
   */
  fun strip(field: String): String

  /**
   * @param field the Field to process.
   * @return Returns a field in the syntax format.
   */
  fun format(field: String): String

  /**
   * @param string The string to test.
   * @return Returns true if the string is a field.
   */
  fun isField(string: String?): Boolean

  /**
   * @param field The field to test.
   * @return Returns true if the field needs to resolve post-load.
   */
  fun isResolve(field: String): Boolean

  /**
   * @param field The field to test.
   * @return Returns true if the field forces queries of it to only be
   *     at the package scope.
   */
  fun isPackageScope(field: String): Boolean

  /**
   * @param list The list to test.
   * @return Returns true if any string in the list contains a field
   *     that needs to walk.
   */
  fun needsWalk(list: List<*>): Boolean {
    for (string in list) if (string != null && needsWalk(string)) return true
    return false
  }

  /**
   * @param value The value to test.
   * @return Returns true if the
   */
  fun needsWalk(value: Any): Boolean {
    val valueActual = StringUtil.toAString(value)
    val fields = getFields(valueActual)
    for (field in fields) if (field.resolve) return true
    return false
  }
}
