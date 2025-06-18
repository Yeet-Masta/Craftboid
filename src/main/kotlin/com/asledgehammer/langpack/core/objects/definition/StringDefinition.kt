package com.asledgehammer.langpack.core.objects.definition

import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.objects.LangGroup
import com.asledgehammer.langpack.core.objects.formatter.FieldFormatter

/**
 * **StringDefinition** wraps and handles definitions of strings stored
 * in [LangGroup].
 *
 * @author Jab
 */
class StringDefinition : LangDefinition<String> {

  /**
   * Full constructor.
   *
   * @param pack The pack that the definition belongs to.
   * @param parent The parent that the definition belongs to.
   * @param value The value of the definition.
   */
  constructor(pack: LangPack, parent: LangGroup?, value: String) : super(pack, parent, value)

  /**
   * Minimal constructor.
   *
   * The definition has no parent.
   *
   * @param pack The pack that the definition belongs to.
   * @param value The value of the definition.
   */
  constructor(pack: LangPack, value: String) : super(pack, null, value)

  override fun onWalk(): String = walk(raw)
  override fun needsWalk(formatter: FieldFormatter): Boolean = formatter.needsWalk(raw)
  override fun toString(): String = "StringDefinition(raw=$raw, value=$value)"
}
