package com.asledgehammer.langpack.core.objects

import com.asledgehammer.crafthammer.util.cfg.YamlFile
import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.Language
import java.io.File

/**
 * **LangFile** is a [LangGroup] that represents an entire file. A
 * locale is assigned to resolve definitions based on the [Language]
 * specified.
 *
 * @author Jab
 */
class LangFile : LangGroup {

  /** The file storing the YAML data. */
  var file: File? = null

  /**
   * Runtime constructor.
   *
   * @param pack The pack instance.
   * @param language The language of the file.
   * @param name The name of the section.
   */
  constructor(pack: LangPack, language: Language, name: String) : super(pack, language, name)

  /**
   * File constructor.
   *
   * @param pack The pack instance.
   * @param file The file to read.
   * @param language The language of the file.
   */
  constructor(pack: LangPack, file: File, language: Language) : super(pack, language, file.nameWithoutExtension) {
    require(file.exists()) { "File not found: ${file.path}" }
    this.file = file
  }

  override fun toString(): String = "LangFile(lang=$language)"

  /**
   * Attempts to parse the File to YAML data to read as a lang section.
   *
   * @return Returns the instance of the file for single-line
   *     executions.
   */
  fun load(): LangFile {
    // Clear the current entries and reload from file.
    clear()
    if (file != null) append(YamlFile(file!!).read())
    return this
  }

  /**
   * Appends a YAML file.
   *
   * @param file The file to parse and append.
   * @return Returns the instance of the file for single-line
   *     executions.
   */
  fun append(file: File): LangFile {
    append(YamlFile(file).read())
    return this
  }
}
