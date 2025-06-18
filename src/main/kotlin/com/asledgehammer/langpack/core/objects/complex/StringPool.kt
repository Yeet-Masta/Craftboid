@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.asledgehammer.langpack.core.objects.complex

import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.Language
import com.asledgehammer.langpack.core.objects.LangArg
import com.asledgehammer.langpack.core.objects.LangGroup
import com.asledgehammer.langpack.core.objects.complex.StringPool.Mode
import com.asledgehammer.langpack.core.objects.definition.ComplexDefinition
import com.asledgehammer.langpack.core.objects.definition.LangDefinition
import com.asledgehammer.langpack.core.objects.formatter.FieldFormatter
import com.asledgehammer.langpack.core.util.StringUtil
import java.util.*

/**
 * **StringPool** allows for storage of multiple strings to be polled
 * based on a set [Mode].
 *
 * @author Jab
 */
open class StringPool : Complex<String> {

  override var definition: ComplexDefinition? = null

  /** The method of polling for the pool. */
  val mode: Mode

  /** The random instance for the pool. */
  var random: Random

  private var strings = ArrayList<String>()
  private var index = 0

  /**
   * Empty constructor.
   *
   * Uses default mode of [StringPool.Mode.RANDOM]. Uses default random
   * instance from LangPack.
   */
  constructor() : this(Mode.RANDOM, LangPack.DEFAULT_RANDOM)

  /**
   * Lite constructor.
   *
   * Uses default random instance from LangPack.
   *
   * @param mode The mode of the StringPool. (DEFAULT: [Mode.RANDOM])
   */
  constructor(mode: Mode) : this(mode, LangPack.DEFAULT_RANDOM)

  /**
   * Basic constructor.
   *
   * @param mode The mode of the StringPool. (DEFAULT: [Mode.RANDOM])
   * @param random The random instance to use.
   */
  constructor(mode: Mode, random: Random) {
    this.mode = mode
    this.random = random
  }

  /**
   * Full constructor.
   *
   * @param mode (Optional) The mode of the StringPool. (DEFAULT:
   *     [Mode.RANDOM])
   * @param random (Optional) The random instance to use.
   * @param strings The pool of strings to use.
   */
  constructor(mode: Mode, random: Random, strings: Collection<String>) {
    this.mode = mode
    this.random = random
    this.strings = ArrayList(strings)
  }

  /**
   * Import constructor.
   *
   * Uses default random instance from LangPack.
   *
   * @param cfg The ConfigurationSection to load.
   */
  constructor(cfg: CFGSection) {
    var mode: Mode = Mode.RANDOM
    this.random = LangPack.DEFAULT_RANDOM

    // Load the mode if defined.
    if (cfg.contains("mode")) {
      val modeCheck: Mode? = Mode.getType(cfg.getString("mode"))
      if (modeCheck == null) {
        System.err.println("""The mode "$mode" is an invalid StringPool mode. Using ${mode.name}.""")
      } else {
        mode = modeCheck
      }
    }
    this.mode = mode

    val list = cfg.getList("pool")
    if (list.isNotEmpty()) {
      for (o in list) {
        if (o != null) {
          add(StringUtil.toAString(o))
        } else {
          add("")
        }
      }
    }
  }

  override fun process(pack: LangPack, lang: Language, context: LangGroup?, vararg args: LangArg): String {
    return if (strings.isEmpty()) {
      ""
    } else {
      pack.processor.process(poll(), pack, lang, context, *args)
    }
  }

  override fun walk(definition: LangDefinition<*>): StringPool = StringPool(mode, random, definition.walk(strings))

  override fun needsWalk(formatter: FieldFormatter): Boolean = formatter.needsWalk(strings)

  override fun get(): String {
    return if (strings.isEmpty()) {
      ""
    } else {
      poll()
    }
  }

  /** @return Returns the next result in the pool. */
  fun poll(): String {
    require(strings.isNotEmpty()) { "The StringPool is empty and cannot poll." }
    return strings[roll()]
  }

  /** @return Returns the next string-index to use. */
  fun roll(): Int {
    if (strings.isEmpty()) return -1
    return when (mode) {
      Mode.RANDOM -> {
        random.nextInt(strings.size)
      }
      Mode.SEQUENTIAL -> {
        val result = index++
        if (index == strings.size) index = 0
        result
      }
      Mode.SEQUENTIAL_REVERSED -> {
        val result = index--
        if (index == -1) index = strings.lastIndex
        result
      }
    }
  }

  /**
   * Adds a string to the pool.
   *
   * @param string The string to add.
   */
  fun add(string: String) {
    strings.add(string)
    index = if (mode == Mode.SEQUENTIAL_REVERSED) {
      strings.lastIndex
    } else {
      0
    }
  }

  /** Clears all strings from the pool. */
  fun clear() {
    strings.clear()
    index = 0
  }

  /** @return Returns true if the StringPool is empty. */
  fun isEmpty(): Boolean = strings.isEmpty()

  /**
   * **Mode** identifies the method of rolling for [StringPool].
   *
   * @author Jab
   */
  enum class Mode {
    RANDOM,
    SEQUENTIAL,
    SEQUENTIAL_REVERSED;

    companion object {

      /**
       * @param id The id of the Mode.
       * @return Returns the mode that identifies with the one provided.
       *     If no mode-identity matches the one provided, null is returned.
       */
      fun getType(id: String): Mode? {
        if (id.isNotEmpty()) {
          for (next in values()) {
            if (next.name.equals(id, true)) return next
          }
        }
        return null
      }
    }
  }

  /**
   * **StringPool.Loader** loads [StringPool] from YAML with the
   * assigned type *pool*.
   *
   * @author Jab
   */
  class Loader : Complex.Loader<StringPool> {
    override fun load(cfg: CFGSection): StringPool = StringPool(cfg)
  }
}
