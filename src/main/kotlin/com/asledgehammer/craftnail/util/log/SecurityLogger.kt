package com.asledgehammer.craftnail.util.log

import com.asledgehammer.crafthammer.util.console.ANSIUtils
import com.asledgehammer.craftnail.CraftNail
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.text.Charsets
import zombie.ZomboidFileSystem
import zombie.debug.DebugLog
import zombie.debug.DebugType

/**
 * Logs security-related events to a file.
 */
object SecurityLogger {
    // File to write logs to
    private lateinit var file: File
    
    // Thread for asynchronous logging
    private lateinit var thread: Thread
    
    // Whether the logger is running
    private var running = false
    
    // Date format for log entries
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS")
    
    // Queue of log entries to write
    private val queue = ArrayList<Entry>()
    
    /**
     * Initializes the security logger.
     */
    fun init() {
        try {
            // Create log directory if it doesn't exist
            val dir = File("${ZomboidFileSystem.instance.getCacheDir()}${File.separator}Logs${File.separator}Craftboid")
            
            if (!dir.exists() && !dir.mkdirs()) {
                throw RuntimeException("Cannot make directory: ${dir.path}")
            }
            
            // Create log file
            file = File(dir, "security.log")
            
            if (!file.exists()) {
                file.createNewFile()
            }
            
            // Start logging thread
            thread = Thread({ 
                while (running) {
                    Thread.sleep(1000)
                    poll()
                }
                poll() // One final poll when stopped
            }, "Security_Logger_Thread")
            
            thread.start()
            running = true
        } catch (e: Exception) {
            CraftNail.logError("Failed to initialize security logger. Security events will not be logged.", e)
        }
    }
    
    /**
     * Stops the security logger.
     */
    fun stop() {
        running = false
        poll() // One final poll
    }
    
    /**
     * Queues a log entry to be written to the log file.
     * 
     * @param date The date of the log entry.
     * @param line The log message.
     */
    fun queue(date: Date, line: String) {
        if (running) {
            synchronized(queue) {
                queue.add(Entry(date, line))
            }
        }
    }
    
    /**
     * Writes queued log entries to the log file.
     */
    private fun poll() {
        synchronized(queue) {
            if (queue.isEmpty()) {
                return
            }
            
            // Open file for writing (append mode)
            OutputStreamWriter(FileOutputStream(file, true), Charsets.UTF_8).use { outputStreamWriter ->
                val writer = BufferedWriter(outputStreamWriter, 8192)
                
                try {
                    // Write each entry
                    for (entry in queue) {
                        writer.write(entry.toString())
                        writer.newLine()
                    }
                    
                    // Clear queue
                    queue.clear()
                } finally {
                    // Ensure writer is closed
                    writer.close()
                }
            }
        }
    }
    
    /**
     * Logs messages to the debug console and queues them for the log file.
     * 
     * @param list The list of messages to log.
     */
    fun log(list: List<Any?>) {
        if (list.isEmpty()) {
            DebugLog.log(DebugType.Security, "")
            return
        }
        
        for (o in list) {
            if (o == null) {
                DebugLog.log(DebugType.Security, "")
            } else {
                DebugLog.log(DebugType.Security, o.toString())
            }
        }
    }
    
    /**
     * Logs messages to the debug console and queues them for the log file.
     * 
     * @param objects The messages to log.
     */
    fun log(vararg objects: Any?) {
        if (objects.isEmpty()) {
            DebugLog.log(DebugType.Security, "")
            return
        }
        
        for (o in objects) {
            if (o == null) {
                DebugLog.log(DebugType.Security, "")
            } else {
                DebugLog.log(DebugType.Security, o.toString())
            }
        }
    }
    
    /**
     * A log entry with a timestamp and message.
     */
    private class Entry(val date: Date, val line: String) {
        override fun toString(): String {
            return "[${dateFormat.format(date)}] ${ANSIUtils.strip(line)}"
        }
    }
}
