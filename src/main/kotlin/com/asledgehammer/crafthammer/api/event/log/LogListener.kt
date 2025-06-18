package com.asledgehammer.crafthammer.api.event.log

interface LogListener {
  fun onLogEntry(entry: LogEntry)
}