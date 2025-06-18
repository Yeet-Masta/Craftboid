package com.asledgehammer.langpack.core.objects

/**
 * **LangArg** is a struct for a storage of key->value pairs to override
 * & replace fields.
 *
 * @author Jab
 * @property key The key to identify.
 * @property value The value to store.
 */
data class LangArg(val key: String, val value: Any) {
  override fun toString(): String = "LangArg(key='$key', value='$value')"
}
