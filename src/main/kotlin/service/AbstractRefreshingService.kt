package service

import view.Refreshable

abstract class AbstractRefreshingService {

    private val refreshables = mutableListOf<Refreshable>()

    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables.add(newRefreshable)
    }

    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }
}