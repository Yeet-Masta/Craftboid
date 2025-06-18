@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.asledgehammer.langpack.core.objects

import com.asledgehammer.crafthammer.util.cfg.CFGSection
import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.Language
import com.asledgehammer.langpack.core.objects.complex.Complex
import com.asledgehammer.langpack.core.objects.complex.StringPool
import com.asledgehammer.langpack.core.objects.definition.ComplexDefinition
import com.asledgehammer.langpack.core.objects.definition.LangDefinition
import com.asledgehammer.langpack.core.objects.definition.StringDefinition
import com.asledgehammer.langpack.core.util.StringUtil
import java.io.File
import java.util.*

/**
 * **LangGroup** is a hierarchical solution for definitions stored as
 * fields. Groups are mutable and assignable to other groups.
 *
 * Fields are formatted as lower-case strings when registered and
 * queried. Fields can reference nested objects using periods as a
 * delimiter.
 *
 * ###
 * **Examples**: **``example_field``**,
 * **``example_group.example_field``**
 *
 * @author Jab
 * @property pack The pack instance.
 * @property language The language that the group belongs to.
 * @property name The name of the group.
 * @property parent (Optional) The parent group.
 */
open class LangGroup(var pack: LangPack, val language: Language, val name: String, var parent: LangGroup? = null) {

  /** The metadata for the lang group. (Used for imports) */
  val meta = Metadata()

  /** All subgroups are registered to this map. */
  val children = HashMap<String, LangGroup>()

  /**
   * The stored fields for the lang group. Fields are stored as
   * lower-case.
   */
  val fields = HashMap<String, LangDefinition<*>>()

  /**
   * Appends YAML data by reading it and adding it to the lang group.
   *
   * @param cfg The YAML data to read.
   * @return Returns the instance for single-line executions.
   */
  open fun append(cfg: YamlFile): LangGroup {
    append(cfg, meta)
    return this
  }

  /**
   * Reads YAML data, processing it into the lang group.
   *
   * @param cfg The YAML data to read.
   * @param metadata The metadata object to process while reading the
   *     YAML data.
   * @return Returns the instance for single-line executions.
   */
  fun append(cfg: CFGSection, metadata: Metadata = Metadata()): LangGroup {
    if (cfg.isSection("__metadata__")) {
      metadata.read(cfg.getSection("__metadata__"))
      // Load imports prior to in-file fields, potentially overriding an import.
      if (metadata.imports.isNotEmpty()) {
        val localDir = pack.dir
        for (next in metadata.imports) {
          var import = next
          if (!import.endsWith(".yml", true)) import += ".yml"
          // Try local file paths first.
          var importFile = File(localDir, import)
          if (importFile.exists()) {
            if (pack.debug) println("[$name] :: Loading import: ${importFile.path}")
            append(YamlFile(importFile).read())
            continue
          }
          // Try absolute path second.
          importFile = File(import)
          if (!importFile.exists()) {
            if (pack.debug) System.err.println("Cannot import language file: $import (Not found)")
            continue
          }
          if (pack.debug) println("[$name] :: Loading import: ${importFile.path}")
          append(YamlFile(importFile).read())
        }
      }
    }

    for (key in cfg.childKeys) {
      // Exclude the metadata group.
      if (key.equals("__metadata__", true)) continue

      val group = cfg.getSection(key)
      if (group.isString("type")) {
        readComplex(group)
      } else {
        readGroup(group)
      }
    }

    for (key in cfg.fieldKeys) {
      val raw = StringUtil.toAString(cfg.get(key))
      set(key, StringDefinition(pack, this, raw))
    }
    return this
  }

  /**
   * Processes a YAML section as a lang group.
   *
   * @param cfg The YAML section to read.
   */
  private fun readGroup(cfg: CFGSection) {
    val langSection = LangGroup(pack, language, cfg.name.lowercase(Locale.getDefault()), this)
    langSection.append(cfg, Metadata())
    setChild(langSection)
  }

  /**
   * Attempts to read a YAML section as a complex object. All complex
   * objects are YAML sections with a defined **type** string. If the
   * field doesn't exist or is not a string, the YAML section is loaded
   * as a lang group.
   *
   * @param cfg The YAML section to read.
   */
  private fun readComplex(cfg: CFGSection) {
    if (!cfg.contains("type") || !cfg.isString("type")) {
      readGroup(cfg)
      return
    }
    val type = cfg.getString("type")
    val loader = pack.getLoader(type)
    if (loader != null) {
      set(cfg.name, ComplexDefinition(pack, this, loader.load(cfg)))
    } else {
      System.err.println("Unknown complex type: $type")
    }
  }

  /**
   * Attempts to locate a stored value with a query.
   *
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns the resolved query. If nothing is located at the
   *     destination of the query, null is returned.
   */
  fun resolve(query: String): LangDefinition<*>? {

    if (pack.debug) println("[$name] :: resolve($query)")

    if (query.contains(".")) {

      val split = query.split(".")

      val groupId = split[0]

      // Make sure
      val raw = children[groupId.lowercase(Locale.getDefault())] ?: return null

      var rebuiltQuery = split[1]
      if (split.size > 2) {
        for (index in 2..split.lastIndex) {
          rebuiltQuery += ".${split[index]}"
        }
      }

      if (pack.debug) println("[$name] :: rebuiltQuery: $rebuiltQuery")

      return raw.resolve(rebuiltQuery)

    } else {
      return fields[query.lowercase(Locale.getDefault())]
    }
  }

  fun resolveGroup(query: String): LangGroup? {

    if (pack.debug) println("[$name] :: resolve($query)")

    if (query.contains(".")) {

      val split = query.split(".")

      val groupId = split[0]

      // Make sure
      val raw = children[groupId.lowercase(Locale.getDefault())] ?: return null

      var rebuiltQuery = split[1]
      if (split.size > 2) {
        for (index in 2..split.lastIndex) {
          rebuiltQuery += ".${split[index]}"
        }
      }

      if (pack.debug) println("[$name] :: rebuiltQuery: $rebuiltQuery")

      return raw.resolveGroup(rebuiltQuery)

    } else {
      return children[query.lowercase(Locale.getDefault())]
    }
  }

  /** Walks all children groups and fields. Used for post-loading. */
  fun walk() {
    for ((_, child) in children) child.walk()
    for ((_, field) in fields) field.walk()
  }

  /**
   * Un-Walks all children groups and fields. These resets modified
   * definitions to their original loaded state.
   */
  fun unWalk() {
    for ((_, child) in children) child.unWalk()
    for ((_, field) in fields) field.unWalk()
  }

  /** Clears all fields and subgroups in the group. */
  fun clear() {
    children.clear()
    fields.clear()
  }

  /**
   * Attempts to resolve a string with a query.
   *
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns the resolved query. If nothing is located at the
   *     destination of the query, null is returned.
   */
  fun getString(query: String): String? {
    val o = resolve(query) ?: return null
    if (o is ComplexDefinition) {
      return o.value.get().toString()
    } else if (o is StringDefinition) {
      return o.value
    }
    return null
  }

  /**
   * Attempts to resolve a lang group with a query.
   *
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns the resolved query.
   * @throws RuntimeException Thrown if the query is unresolved or the
   *     resolved object is not a lang group.
   */
  fun getChild(query: String): LangGroup = resolveGroup(query)!!

  /**
   * Sets a child group.
   *
   * @param group The child to set.
   */
  fun setChild(group: LangGroup) {
    children[group.name] = group
  }

  /**
   * Removes a child group from the group.
   *
   * @param id The id of the child.
   */
  fun removeChild(id: String) {
    children.remove(id.lowercase(Locale.getDefault()))
  }

  /**
   * Attempts to resolve a string pool with a query.
   *
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns the resolved query.
   * @throws RuntimeException Thrown if the query is unresolved or the
   *     resolved object is not a lang group.
   */
  fun getStringPool(query: String): StringPool {
    val value = resolve(query)
    require(value != null && value.value is StringPool) { "The field $query is not a StringPool." }
    return value.value as StringPool
  }

  /**
   * @return Returns the full path to the group, appending their
   *     parent's path. (If exists)
   */
  fun getPath(): String {
    return if (parent != null && parent !is LangFile) {
      "${parent!!.getPath()}.$name"
    } else {
      name
    }
  }

  /**
   * Assigns a value with an ID.
   *
   * @param key The ID to assign the value.
   * @param value The value to assign to the ID.
   */
  fun set(key: String, value: LangDefinition<*>?) {

    if (pack.debug) println("[$name] :: set($key, $value)")

    if (key.contains(".")) {

      val split = key.split(".")
      val groupId = split[0].lowercase(Locale.getDefault())

      var raw = children[groupId]
      if (raw !is LangGroup) {
        raw = LangGroup(pack, language, groupId, this)
        children[groupId] = raw
      }

      var rebuiltQuery = split[1]
      if (split.size > 2) {
        for (index in 2..split.lastIndex) {
          rebuiltQuery += ".${split[index]}"
        }
      }

      return raw.set(rebuiltQuery, value)

    } else {
      if (value != null) {
        fields[key.lowercase(Locale.getDefault())] = value
      } else {
        fields.remove(key.lowercase(Locale.getDefault()))
      }
    }
  }

  /**
   * Removes a field from the group.
   *
   * @param id The id of the field.
   */
  fun remove(id: String) {
    fields.remove(id.lowercase(Locale.getDefault()))
  }

  /**
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns true if the query resolves.
   */
  fun contains(query: String): Boolean = fields.containsKey(query.lowercase(Locale.getDefault()))

  /**
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns true if the query resolves and is the type
   *     [Complex].
   */
  fun isComplex(query: String): Boolean = contains(query) && fields[query.lowercase(Locale.getDefault())] is Complex<*>

  /**
   * @param query The string to process. The string can be a field or
   *     set of fields delimited by a period.
   * @return Returns true if the query resolves and is the type
   *     [StringPool].
   */
  fun isStringPool(query: String): Boolean {
    if (!contains(query)) return false
    val field = fields[query.lowercase(Locale.getDefault())]
    return field != null && field.value is StringPool
  }

  /**
   * **Metadata** handles all metadata defined for [LangGroup].
   *
   * Metadata is defined as a YAML section inside the group.
   *
   * Example:
   *
   * ```yml
   * group:
   *   __metadata__:
   *     import: ..
   *     # OR
   *     imports: [..]
   * ```
   *
   * Metadata supports importing from other lang-files. Formats are as
   * follows:
   * - **import:** Imports only one file as a string.
   * - **imports:** Imports multiple files as a string-list.
   *
   * Additionally, file-names can be given without extensions,
   * however the files stored must have the .yml extension. Files can
   * be referenced by name in the same location as the lang-pack's
   * directory. Otherwise, Java-File supported paths will be tried to
   * locate the lang-file.
   *
   * @author Jab
   */
  class Metadata {

    /** The import files defined in the metadata. */
    val imports = ArrayList<String>()

    /**
     * Reads a YAML section as metadata.
     *
     * @param cfg The YAML section to read.
     */
    fun read(cfg: CFGSection) {
      if (cfg.contains("imports")) {
        if (cfg.isList("imports")) {
          for (next in cfg.getStringList("imports")) {
            imports.add(next)
          }
        }
      } else if (cfg.contains("import")) {
        if (cfg.isString("import")) imports.add(cfg.getString("import"))
      }
    }
  }
}
