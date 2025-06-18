package com.asledgehammer.crafthammer.api.event

/**
 * **EventHandler** TODO: Document.
 *
 * @author Jab
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class EventHandler(
  val priority: Int = 0,
  val ignoreCancelled: Boolean = false,
)
