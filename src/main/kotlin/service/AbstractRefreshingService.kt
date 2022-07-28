package service

import view.Refreshable

/**
 * This is an abstract class to hold the [Refreshable] objects and provide two functions for adding a [Refreshable] and
 * executing a refresh function on all [Refreshable] objects
 */
abstract class AbstractRefreshingService {

    /**
     * List of all [Refreshable] objects
     */
    private val refreshables = mutableListOf<Refreshable>()

    /**
     * This is a function for adding a [Refreshable] object to the [MutableList] refreshables
     *
     * @param newRefreshable [Refreshable] object to be added to the list
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables.add(newRefreshable)
    }

    /**
     * This is a function for executing a function on all [Refreshable] objects in the list
     *
     * @param method function to be executed
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }
}