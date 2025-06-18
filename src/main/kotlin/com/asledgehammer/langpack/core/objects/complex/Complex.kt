package com.asledgehammer.langpack.core.objects.complex

import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.Language
import com.asledgehammer.langpack.core.objects.LangArg
import com.asledgehammer.langpack.core.objects.LangGroup
import com.asledgehammer.langpack.core.objects.definition.ComplexDefinition
import com.asledgehammer.langpack.core.objects.definition.LangDefinition
import com.asledgehammer.langpack.core.objects.formatter.FieldFormatter
import com.asledgehammer.langpack.core.processor.LangProcessor

/**
 * **Complex** serves as a wrapper for non-primitive objects utilized in
 * [LangPack]. Complex objects construct and resolve as Defined by type
 * **E**.
 *
 * @author Jab
 * @param E The type to resolve when queried.
 */
interface Complex<E> {

  /**
   * 'definition' is a reference point for relative fields when
   * processed.
   */
  var definition: ComplexDefinition?

  /**
   * Walks the complex object. This is a post-load operation where
   * operations such as resolve-fields are processed.
   *
   * @param definition The instance of the definition walked.
   */
  fun walk(definition: LangDefinition<*>): Complex<E>

  /**
   * Process the complex object using the lang-pack's [LangProcessor].
   *
   * @param pack The lang-pack instance.
   * @param lang The language to query.
   * @param context (Optional) The context group to look up. If not
   *     passed, the process will interpret look-ups on the package level.
   * @param args (Optional) Arguments to pass to the processor.
   * @return Returns the processed result.
   */
  fun process(pack: LangPack, lang: Language, context: LangGroup?, vararg args: LangArg): E

  /**
   * @param formatter The formatter to process and identify fields
   *     defined throughout the complex object.
   * @return Returns true if the complex object needs to process
   *     post-load.
   */
  fun needsWalk(formatter: FieldFormatter): Boolean

  /**
   * Process the complex object.
   *
   * @return Returns the processed result.
   */
  fun get(): E

  /**
   * **Loader** allows third-party installments of complex objects that
   * require code extensions in specific environments.
   *
   * @author Jab
   */
  @Suppress("MemberVisibilityCanBePrivate", "unused")
  interface Loader<E : Complex<*>> {

    /**
     * Load from a configured YAML section as an instance.
     *
     * @param cfg The YAML to read.
     * @return Returns the loaded object.
     */
    fun load(cfg: CFGSection): E
  }
}
