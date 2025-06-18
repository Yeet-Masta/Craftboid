@file:Suppress("unused")

package com.asledgehammer.crafthammer.api.command

/**
 * **CommandExecution** TODO: Document.
 *
 * @author Jab
 */
class CommandExecution(val command: Command, val commander: CommandSender, val response: Command.Response) {

  fun accept(message: String = "") {
    response.accept(message)
  }

  fun deny(message: String = "") {
    response.deny(message)
  }
}
