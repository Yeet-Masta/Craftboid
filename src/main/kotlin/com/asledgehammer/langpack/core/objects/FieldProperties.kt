package com.asledgehammer.langpack.core.objects

/**
 * **FieldProperties** handles information for field inputs for strings.
 * This sanitizes flags as fields to support multiple formats without
 * hard-coding the disassembly of one format's syntax.
 *
 * @author Jab
 * @property raw The unprocessed field.
 * @property name The name of the field.
 * @property placeholder The placeholder to use if the field is not
 *     resolved.
 * @property resolve If set to true, the field needs to resolve during
 *     the *walk* cycle, immediately following loading its package.
 * @property packageScope If set to true, queries for this field will
 *     start on the package-level scope. (Non-relative lookup)
 */
class FieldProperties(
  val raw: String,
  val name: String,
  val placeholder: String,
  val resolve: Boolean,
  val packageScope: Boolean,
) {
  override fun toString(): String =
    "FieldProperties(raw='$raw', name='$name', placeholder='$placeholder', resolve=$resolve, packageScope=$packageScope)"
}
