package com.asledgehammer.craftnail.packet.security

import java.util.Locale

/**
 * The mode for security checks.
 */
enum class Mode {
    /** Disable the check entirely. */
    OFF,
    
    /** Log violations but don't kick players. */
    IGNORE,
    
    /** Log violations and kick players. */
    KICK;
    
    companion object {
        /**
         * Gets a mode from a string.
         * 
         * @param id The string representation of the mode.
         * @return The corresponding Mode enum value.
         */
        operator fun get(id: String): Mode {
            if (id.isEmpty()) {
                throw IllegalArgumentException("The id is empty.")
            }
            
            val idUpperCase = id.uppercase(Locale.getDefault()).trim()
            
            // Handle special case for FALSE
            if (idUpperCase == "FALSE") {
                return OFF
            }
            
            // Try to match with enum values
            for (mode in values()) {
                if (mode.name == idUpperCase) {
                    return mode
                }
            }
            
            // Default to IGNORE if no match found
            return IGNORE
        }
    }
}
