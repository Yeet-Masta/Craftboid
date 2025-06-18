@file:Suppress("unused", "UNCHECKED_CAST")

package com.asledgehammer.crafthammer

import ReflectionUtils
import com.asledgehammer.crafthammer.api.event.*
import com.asledgehammer.crafthammer.api.event.EventListener
import java.lang.reflect.Method
import java.util.*

/**
 * **CraftEvents** TODO: Document.
 *
 * @author Jab
 */
class CraftEvents : Events {

  private val wrappers = HashMap<UUID, CraftWrapper>()
  private val sortedEventWrappers = HashMap<Class<out Event>, ArrayList<EventHandleWrapper>>()
  private val eventWrappersToRemove = ArrayList<EventHandleWrapper>()

  override fun add(id: UUID, listener: EventListener) {
    wrappers.computeIfAbsent(id) { CraftWrapper(id) }.register(listener)
    sort()
  }

  override fun removeAll(id: UUID) {
    val wrapper = wrappers.remove(id) ?: return
    wrapper.reset()
  }

  override fun remove(id: UUID, listener: EventListener, sort: Boolean) {
    wrappers[id]?.unregister(listener)
    if (sort) sort()
  }

  override fun dispatch(event: Event) {

    event.handled = false

    if (DEBUG) {
      println("dispatch(${event::class.java.simpleName}) THREAD: ${Thread.currentThread()}")
    }

    val clazz = event.javaClass
    val wrappers = sortedEventWrappers[clazz] ?: return
    for (wrapper in wrappers) {
      try {
        wrapper.dispatch(event)
      } catch (throwable: Throwable) {
        CraftHammer.logError("Failed to execute listener: ${clazz.simpleName}. Disabling listener.", throwable)
        throwable.printStackTrace(System.err)
        wrapper.isEnabled = false
        eventWrappersToRemove.add(wrapper)
      }
    }
    if (eventWrappersToRemove.isNotEmpty()) {
      for (wrapper in eventWrappersToRemove) remove(wrapper.id, wrapper.listener, false)
      sort()
    }

    event.handled = true
  }

  override fun reset() {
    for ((_, wrapper) in wrappers) wrapper.reset()
    wrappers.clear()
  }

  private fun sort() {
    sortedEventWrappers.clear()
    for ((_, wrapper) in wrappers) {
      for ((key, collection) in wrapper.eventWrappers) {
        val next = sortedEventWrappers.computeIfAbsent(key) { ArrayList() }
        for (handle in collection) next.add(handle)
      }
    }
    for ((_, list) in sortedEventWrappers) list.sortByDescending { it.annotation.priority }
  }

  private class CraftWrapper(val id: UUID) : Events.Wrapper {

    override val eventWrappers = HashMap<Class<out Event?>, ArrayList<EventHandleWrapper>>()

    override fun register(listener: EventListener) {
      // Grab the methods for the Listener.
      val methods: List<Method> = ReflectionUtils.getAllDeclaredMethods(listener.javaClass)
      if (methods.isEmpty()) return
      for (method in methods) {
        val eventHandler = method.getAnnotation(EventHandler::class.java)
        if (eventHandler != null) {
          val clazz = method.parameters[0].type as Class<out Event?>
          val wrapper = EventHandleWrapper(id, listener, eventHandler, method)
          eventWrappers.computeIfAbsent(clazz) { ArrayList() }.add(wrapper)
          continue
        }
      }
    }

    override fun unregister(listener: EventListener) {
      val eventsToRemove = ArrayList<Class<out Event>>()
      for ((clazz, wrapper) in eventWrappers) {
        val iterator = wrapper.iterator()
        while (iterator.hasNext()) {
          val next = iterator.next()
          if (next.listener == listener) iterator.remove()
        }
        if (wrapper.isEmpty()) eventsToRemove.add(clazz)
      }
      if (eventsToRemove.isNotEmpty()) {
        for (clazz in eventsToRemove) eventWrappers.remove(clazz)
      }
    }

    override fun reset() {
      for ((_, list) in eventWrappers) {
        val iterator: MutableIterator<EventHandleWrapper> = list.iterator()
        while (iterator.hasNext()) {
          val wrapper: EventHandleWrapper = iterator.next()
          wrapper.isEnabled = false
          iterator.remove()
        }
      }
      eventWrappers.clear()
    }
  }

  companion object {
    var DEBUG = true
  }
}
