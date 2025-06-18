package com.asledgehammer.crafthammer.api.event

import java.lang.reflect.Method
import java.util.*

/**
 * **EventHandleWrapper** TODO: Document.
 *
 * @author Jab
 * @param listener The declaring instance.
 * @param annotation The information for handling the event.
 * @param method The method handler to invoke.
 */
class EventHandleWrapper(val id: UUID, listener: EventListener, annotation: EventHandler, method: Method) :
  HandleWrapper<EventHandler, EventListener, Event>(listener, annotation, method) {

  override fun canDispatch(element: Event): Boolean {
    if (annotation.ignoreCancelled) return true
    return element !is Cancelable || !element.cancelled
  }

  override fun isParameterValid(clazz: Class<*>): Boolean = Event::class.java.isAssignableFrom(clazz)
}
