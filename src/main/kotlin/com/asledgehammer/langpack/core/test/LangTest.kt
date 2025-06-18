@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.asledgehammer.langpack.core.test

import com.asledgehammer.langpack.core.LangPack
import com.asledgehammer.langpack.core.objects.LangArg

/**
 * **LangTest** is a runtime testing utility for [LangPack].
 *
 * @author Jab
 * @property id The name of the test.
 * @property description A brief description of what the test does.
 * @param Pack The pack type.
 * @param Commander The object of the person orchestrating the test.
 */
abstract class LangTest<Pack : LangPack, Commander>(val id: String, val description: List<String>) {

  /**
   * Executes the test procedure.
   *
   * @param pack The lang-pack instance to test.
   * @param commander The commander running the test.
   * @return Returns the results of the test.
   */
  fun test(pack: Pack, commander: Commander, vararg args: LangArg): TestResult {
    val time = System.currentTimeMillis()
    return try {
      val result = run(pack, commander, *args)
      result.time = System.currentTimeMillis() - time
      result
    } catch (e: Exception) {
      e.printStackTrace(System.err)
      val result = TestResult.failure(e.message ?: "The test failed.")
      result.time = System.currentTimeMillis() - time
      result
    }
  }

  /**
   * @param pack The lang-pack instance to test.
   * @param player The player running the test.
   * @return Returns the result of the test.
   */
  protected abstract fun run(pack: Pack, player: Commander, vararg args: LangArg): TestResult
}
