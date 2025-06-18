package com.asledgehammer.craftnail.util

import com.asledgehammer.craftnail.CraftNail
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.Locale
import zombie.ZomboidFileSystem
import zombie.debug.DebugLog
import zombie.debug.DebugType

/**
 * Handles filtering of inappropriate text in chat messages.
 */
object TextFilter {
    // Lists for storing patterns and their regex equivalents
    private val patterns = ArrayList<String>()
    private val patternsRegex = ArrayList<Regex>()
    private val patternsRegexLower = ArrayList<Regex>()
    private val patternsCensored = HashMap<Regex, String>()
    
    /**
     * Initializes the text filter by loading patterns from the filters.txt file.
     */
    fun init() {
        // Clear existing patterns if any
        if (isNotEmpty()) {
            patterns.clear()
            patternsRegex.clear()
            patternsCensored.clear()
        }
        
        // Get the path to the filters file
        val cacheDir = ZomboidFileSystem.instance.getCacheDir()
        val fileFilters = File("$cacheDir${File.separator}Server${File.separator}filters.txt")
        
        // Create the file if it doesn't exist
        if (!fileFilters.exists()) {
            createFile()
            return
        }
        
        try {
            // Read the file
            val fileReader = FileReader(fileFilters)
            val bufferedReader = BufferedReader(fileReader)
            
            var line = bufferedReader.readLine()
            while (line != null) {
                // Trim the line
                val trimmedLine = line.trim()
                
                // Skip empty lines and comments
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    line = bufferedReader.readLine()
                    continue
                }
                
                // Check if the pattern is at least 3 characters
                if (trimmedLine.length >= 3) {
                    patterns.add(trimmedLine)
                } else {
                    println("Craftboid: Filter ignored due to being less than 3 characters in length.")
                }
                
                line = bufferedReader.readLine()
            }
            
            fileReader.close()
        } catch (e: Exception) {
            println("Failed to read from filters.txt.")
            e.printStackTrace(System.err)
        }
        
        // Compile patterns
        if (patterns.isNotEmpty()) {
            for (pattern in patterns) {
                // Create regex for exact match
                val regex = Regex(pattern)
                patternsRegex.add(regex)
                
                // Create lowercase regex for case-insensitive match
                val lowerPattern = pattern.lowercase(Locale.getDefault())
                patternsRegexLower.add(Regex(lowerPattern))
                
                // Create censored version (first character + asterisks + last character)
                val first = pattern[0].toString()
                val body = "*".repeat(maxOf(0, pattern.length - 2))
                val last = regex.pattern[pattern.length - 1]
                
                patternsCensored[regex] = "$first$body$last"
            }
        }
        
        // Log the number of loaded filters
        DebugLog.log(DebugType.Security, "Loaded ${patterns.size} filter phrase(s).")
    }
    
    /**
     * Tests if a string contains any filtered words.
     * 
     * @param string The string to test.
     * @return true if the string contains any filtered words, false otherwise.
     */
    fun test(string: String): Boolean {
        // Quick checks
        if (string.isEmpty() || patterns.isEmpty() || !isNotEmpty()) {
            return false
        }
        
        // Convert to lowercase for case-insensitive matching
        val stringLower = string.lowercase(Locale.getDefault())
        
        // Check against all patterns
        for (regex in patternsRegexLower) {
            if (regex.containsMatchIn(stringLower)) {
                return true
            }
        }
        
        return false
    }
    
    /**
     * Censors filtered words in a string.
     * 
     * @param string The string to censor.
     * @return The censored string.
     */
    fun censor(string: String): String {
        var s = string
        
        // Quick checks
        if (s.isEmpty() || isEmpty()) {
            return s
        }
        
        // Replace filtered words with censored versions
        for (pattern in patternsRegex) {
            s = pattern.replace(s, patternsCensored[pattern]!!)
        }
        
        return s
    }
    
    /**
     * Checks if the filter has any patterns loaded.
     * 
     * @return true if there are any patterns loaded, false otherwise.
     */
    fun isNotEmpty(): Boolean = patterns.isNotEmpty()
    
    /**
     * Checks if the filter has no patterns loaded.
     * 
     * @return true if there are no patterns loaded, false otherwise.
     */
    fun isEmpty(): Boolean = patterns.isEmpty()
    
    /**
     * Gets the number of patterns loaded.
     * 
     * @return The number of patterns loaded.
     */
    fun count(): Int = patterns.size
    
    /**
     * Creates the default filters.txt file.
     */
    private fun createFile() {
        val cacheDir = ZomboidFileSystem.instance.getCacheDir()
        val fileFilters = File("$cacheDir${File.separator}Server${File.separator}filters.txt")
        
        try {
            val fileWriter = FileWriter(fileFilters)
            val bufferedWriter = BufferedWriter(fileWriter)
            
            bufferedWriter.write("# Added phrases here to be blocked from chat & usernames.\n# Filters can only be 3 or more characters in length.\n")
            bufferedWriter.flush()
            bufferedWriter.close()
            
            DebugLog.log(DebugType.Security, "Created '${fileFilters.path}'.")
        } catch (e: Exception) {
            println("Failed to create '${fileFilters.path}'.")
            e.printStackTrace(System.err)
        }
    }
}