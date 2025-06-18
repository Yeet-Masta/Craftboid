package com.asledgehammer.craftnail.hook

import com.asledgehammer.craftnail.CraftNail

/**
 * Manages CraftHook plugins.
 */
object CraftHookManager {
    // List of registered hooks
    private val hooks = ArrayList<CraftHook>()
    
    // State tracking
    private var loadedAll = false
    private var enabledAll = false
    
    /**
     * Loads all registered hooks.
     */
    fun loadAll() {
        synchronized(hooks) {
            if (hooks.isEmpty()) {
                return
            }
            
            // Load each hook
            for (hook in hooks) {
                if (!hook.loaded) {
                    hook.load()
                }
            }
            
            loadedAll = true
        }
    }
    
    /**
     * Enables all registered hooks.
     */
    fun enableAll() {
        synchronized(hooks) {
            if (hooks.isEmpty()) {
                return
            }
            
            // Enable each hook
            for (hook in hooks) {
                if (!hook.running) {
                    hook.enable()
                }
            }
            
            enabledAll = true
        }
    }
    
    /**
     * Ticks all running hooks.
     */
    fun tick() {
        synchronized(hooks) {
            if (hooks.isEmpty()) {
                return
            }
            
            // Tick each running hook
            for (hook in hooks) {
                if (hook.running) {
                    hook.tick()
                }
            }
        }
    }
    
    /**
     * Disables all running hooks.
     */
    fun disableAll() {
        synchronized(hooks) {
            if (hooks.isEmpty()) {
                return
            }
            
            // Disable each running hook
            for (hook in hooks) {
                if (hook.running) {
                    hook.disable()
                }
            }
            
            enabledAll = false
        }
    }
    
    /**
     * Unloads all loaded hooks.
     */
    fun unloadAll() {
        synchronized(hooks) {
            if (hooks.isEmpty()) {
                return
            }
            
            // Unload each loaded hook
            for (hook in hooks) {
                if (hook.loaded) {
                    hook.unload()
                }
            }
            
            loadedAll = false
        }
    }
    
    /**
     * Clears all registered hooks.
     */
    fun clear() {
        synchronized(hooks) {
            hooks.clear()
        }
        
        loadedAll = false
        enabledAll = false
    }
    
    /**
     * Registers a hook.
     * 
     * @param hook The hook to register.
     */
    fun register(hook: CraftHook) {
        // Check if hook is already registered
        require(!hooks.contains(hook)) { "The CraftHook is already registered: ${hook.getId()}" }
        
        // Load the hook
        if (!hook.load()) {
            throw IllegalStateException("The CraftHook failed to load: ${hook.getId()}")
        }
        
        synchronized(hooks) {
            // Add the hook
            hooks.add(hook)
            
            // Load/enable if needed
            if (loadedAll && !hook.loaded) {
                hook.load()
            }
            
            if (enabledAll && !hook.running) {
                hook.enable()
            }
        }
    }
    
    /**
     * Unregisters a hook.
     * 
     * @param hook The hook to unregister.
     */
    fun unregister(hook: CraftHook) {
        // Check if hook is registered
        require(hooks.contains(hook)) { "The CraftHook is not registered: ${hook.getId()}" }
        
        // Check if hook is not running
        require(!hook.running) { "The CraftHook is not running and cannot be unregistered: ${hook.getId()}" }
        
        // Disable and unload if needed
        if (hook.running) {
            hook.disable()
        }
        
        if (hook.loaded) {
            hook.unload()
        }
        
        // Remove the hook
        synchronized(hooks) {
            hooks.remove(hook)
        }
    }
    
    /**
     * Invokes hooks from class names.
     * 
     * @param classes The class names of the hooks to invoke.
     */
    fun invoke(classes: List<String>) {
        for (classPath in classes) {
            try {
                // Load the class
                val clazz = Class.forName(classPath)
                
                // Create an instance
                val hook = clazz.getConstructor().newInstance() as CraftHook
                
                // Register the hook
                register(hook)
            } catch (e: Exception) {
                CraftNail.logError("Failed to invoke CraftHook: $classPath", e)
            }
        }
    }
    
    /**
     * Gets the number of registered hooks.
     * 
     * @return The number of registered hooks.
     */
    fun size(): Int {
        synchronized(hooks) {
            return hooks.size
        }
    }
}