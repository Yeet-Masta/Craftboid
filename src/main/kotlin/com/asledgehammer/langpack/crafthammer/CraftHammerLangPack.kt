package com.asledgehammer.langpack.crafthammer

import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.processor.LangProcessor
import com.asledgehammer.langpack.crafthammer.processor.CraftHammerProcessor
import java.io.File

class CraftHammerLangPack(classLoader: ClassLoader = this::class.java.classLoader, dir: File = File("lang")) :
  LangPack(classLoader, dir) {

  override var processor: LangProcessor = CraftHammerProcessor(formatter)

  /**
   * Basic constructor. Uses the 'lang' directory in the server folder.
   *
   * @param classLoader The classloader to load resources.
   */
  constructor(classLoader: ClassLoader) : this(classLoader, File("lang"))
}