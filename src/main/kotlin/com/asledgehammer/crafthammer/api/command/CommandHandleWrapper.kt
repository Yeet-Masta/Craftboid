package com.asledgehammer.crafthammer.api.command

import com.asledgehammer.crafthammer.api.event.HandleWrapper
import java.lang.reflect.Method
import java.util.*

/**
 * **CommandHandleWrapper** TODO: Document.
 *
 * @author Jab
 * @param listener The declaring instance.
 * @param annotation The information for handling the event.
 * @param method The method handler to invoke.
 */
class CommandHandleWrapper(val id: UUID, listener: CommandListener, annotation: CommandHandler, method: Method) :
  HandleWrapper<CommandHandler, CommandListener, CommandExecution>(listener, annotation, method) {
  override fun canDispatch(element: CommandExecution): Boolean = annotation.ignoreHandled || !element.response.handled
  override fun isParameterValid(clazz: Class<*>): Boolean = CommandExecution::class.java.isAssignableFrom(clazz)
}
