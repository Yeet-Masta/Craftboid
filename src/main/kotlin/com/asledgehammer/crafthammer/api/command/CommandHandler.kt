@file:Suppress("unused")

package com.asledgehammer.crafthammer.api.command

/**
 * **CommandHandler** TODO: Document.
 *
 * @author Jab
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class CommandHandler(
  val commands: Array<String>,
  val permission: Array<String> = ["*"],
  val priority: Int = 0,
  val ignoreHandled: Boolean = false,
)
