@file:Suppress("unused")

package com.asledgehammer.langpack.core.test

/**
 * **TestResult** stores the results for [LangTest]
 *
 * @author Jab
 * @property success Set to true if the test succeeded.
 * @property reason (Optional) The reason the test failed.
 */
@ConsistentCopyVisibility
data class TestResult internal constructor(val success: Boolean, val reason: String? = null) {

  /** A time variable for displaying test times. */
  var time: Long = 0L

  companion object {

    /** @return Returns a successful instance of TestResult. */
    fun success(): TestResult = TestResult(true)

    /**
     * @return Returns an unsuccessful TestResult instance with a
     *     provided reason.
     */
    fun failure(reason: String): TestResult = TestResult(false, reason)
  }
}
