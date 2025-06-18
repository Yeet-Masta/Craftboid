package com.asledgehammer.crafthammer.api.event.log

/**
 * **LogEntry** TODO: Document.
 *
 * @author Jab
 */
data class LogEntry(val timestamp: Long, val type: LogType, val message: String) {
  override fun toString(): String {
    return "LogEntry(timestamp=$timestamp, type=$type, message='$message')"
  }
}