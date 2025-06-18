package com.asledgehammer.langpack.core.objects.definition

import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.objects.LangGroup
import com.asledgehammer.langpack.core.objects.complex.Complex
import com.asledgehammer.langpack.core.objects.formatter.FieldFormatter

/**
 * **ComplexDefinition** wraps and handles definitions of complex
 * objects stored in [LangGroup].
 *
 * @author Jab
 */
class ComplexDefinition : LangDefinition<Complex<*>> {

  /**
   * Full constructor.
   *
   * @param pack The pack that the definition belongs to.
   * @param parent The parent that the definition belongs to.
   * @param value The value of the definition.
   */
  constructor(pack: LangPack, parent: LangGroup?, value: Complex<*>) : super(pack, parent, value)

  /**
   * Minimal constructor.
   *
   * The definition has no parent.
   *
   * @param pack The pack that the definition belongs to.
   * @param value The value of the definition.
   */
  constructor(pack: LangPack, value: Complex<*>) : super(pack, null, value)

  init {
    value.definition = this
  }

  override fun onWalk(): Complex<*> = value.walk(this)

  override fun needsWalk(formatter: FieldFormatter): Boolean = raw.needsWalk(formatter)

  override fun toString(): String = "ComplexDefinition(value=$value)"
}
