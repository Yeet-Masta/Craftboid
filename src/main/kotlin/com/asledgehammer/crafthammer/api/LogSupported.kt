package com.asledgehammer.crafthammer.api

/**
 * **LogSupported** TODO: Document.
 *
 * @author Jab
 */
interface LogSupported {
  fun log(list: List<Any?>)
  fun log(vararg objects: Any?)
  fun logError(message: String, cause: Throwable? = null)
}