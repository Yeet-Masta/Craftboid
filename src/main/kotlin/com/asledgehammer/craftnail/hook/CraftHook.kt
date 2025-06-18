package com.asledgehammer.craftnail.hook

import com.asledgehammer.craftnail.CraftNail

/**
 * Abstract base class for hooks in the CraftNail system.
 * Provides lifecycle management for game hooks.
 */
abstract class CraftHook {

    private var timeLastUpdate: Long = -1

    /**
     * Indicates whether the hook is currently running.
     */
    var running: Boolean = false
        private set

    /**
     * Indicates whether the hook has been loaded.
     */
    var loaded: Boolean = false
        private set

    /**
     * Returns the unique identifier for this hook.
     */
    abstract fun getId(): String

    /**
     * Called when the hook is being loaded.
     * @return true if loading was successful, false otherwise
     */
    abstract fun onLoad(): Boolean

    /**
     * Called when the hook is being enabled.
     * @return true if enabling was successful, false otherwise
     */
    abstract fun onEnable(): Boolean

    /**
     * Called on each tick while the hook is running.
     * @param delta The time in milliseconds since the last tick
     */
    abstract fun onTick(delta: Long)

    /**
     * Called when the hook is being disabled.
     */
    abstract fun onDisable()

    /**
     * Called when the hook is being unloaded.
     */
    abstract fun onUnload()

    /**
     * Loads the hook.
     * @return true if loading was successful, false otherwise
     */
    internal fun load(): Boolean {
        require(!loaded) { "Cannot load CraftHook '${getId()}' because it is already loaded." }

        try {
            loaded = onLoad()
        } catch (throwable: Throwable) {
            CraftNail.logError("Failed to load the CraftHook: ${getId()}", throwable)
        }

        return loaded
    }

    /**
     * Enables the hook.
     * @return true if enabling was successful, false otherwise
     */
    internal fun enable(): Boolean {
        require(loaded) { "Cannot start CraftHook '${getId()}' because it is not loaded." }
        require(!running) { "Cannot start CraftHook '${getId()}' because it is already running." }

        try {
            running = onEnable()
        } catch (throwable: Throwable) {
            CraftNail.logError("Failed to start the CraftHook: ${getId()}", throwable)
        }

        return running
    }

    /**
     * Updates the hook.
     */
    internal fun tick() {
        require(running) { "Cannot update CraftHook '${getId()}' because it is not running." }

        val timeNow = System.currentTimeMillis()
        val delta = if (timeLastUpdate == -1L) 0 else timeNow - timeLastUpdate

        try {
            onTick(delta)
        } catch (throwable: Throwable) {
            CraftNail.logError("Failed to update the CraftHook: ${getId()}", throwable)
        }

        timeLastUpdate = timeNow
    }

    /**
     * Disables the hook.
     */
    internal fun disable() {
        require(running) { "Cannot stop CraftHook '${getId()}' because it is not running." }

        try {
            onDisable()
        } catch (throwable: Throwable) {
            CraftNail.logError("Failed to stop the CraftHook: ${getId()}", throwable)
        }

        running = false
    }

    /**
     * Unloads the hook.
     */
    internal fun unload() {
        require(!running && loaded) { "Cannot unload CraftHook '${getId()}' because it is either running or not loaded." }

        try {
            onUnload()
        } catch (throwable: Throwable) {
            CraftNail.logError("Failed to unload the CraftHook: ${getId()}", throwable)
        }

        loaded = false
    }
}