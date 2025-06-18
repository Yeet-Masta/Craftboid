@file:Suppress("PropertyName")

package com.asledgehammer.crafthammer.util.component

import java.util.*

/**
 * **Component** TODO: Document.
 *
 * @author Jab
 */
open class Component<E : Component<E>> {

  /**
   * @return Returns a read-only list of children registered to the component.
   */
  val children: List<E>
    get() = Collections.unmodifiableList(_children)

  /**
   * The internal children map that's modifiable.
   */
  protected var _children = ArrayList<E>()

  /**
   * Appends a component to the component's children.
   *
   * @param component The component to append.
   */
  fun append(component: E) {
    this._children.add(component)
  }

  /**
   * Prepends a component to the component's children.
   *
   * @param component The component to prepend.
   */
  fun prepend(component: E) {
    this._children.add(0, component)
  }
}