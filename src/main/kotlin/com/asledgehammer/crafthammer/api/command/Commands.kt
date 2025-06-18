package com.asledgehammer.crafthammer.api.command

import java.util.*

/**
 * **Commands** TODO: Document.
 *
 * @author Jab
 */
interface Commands {
  fun add(id: UUID, listener: CommandListener)
  fun removeAll(id: UUID)
  fun remove(id: UUID, listener: CommandListener, sort: Boolean = true)
  fun dispatch(command: Command, commander: CommandSender): CommandExecution
  fun reset()

  /**
   * **Commands.Wrapper** TODO: Document.
   *
   * @author Jab
   */
  interface Wrapper {
    val commandWrappers: HashMap<String, ArrayList<CommandHandleWrapper>>
    fun register(listener: CommandListener)
    fun unregister(listener: CommandListener)
    fun reset()
  }
}