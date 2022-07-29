package service

import entity.Card
import view.Refreshable


/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable: Refreshable {

    var refreshAfterStartNewGameCalled: Boolean = false
        private set

    var refreshAfterSwitchOneCalled: Boolean = false
        private set

    var refreshAfterSwitchAllCalled: Boolean = false
        private set

    var refreshAfterPassCalled: Boolean = false
        private set

    var refreshAfterTableDeckChangeCalled: Boolean = false
        private set

    var refreshAfterCloseCalled: Boolean = false
        private set

    var refreshAfterPlayerSwitchCalled: Boolean = false
        private set

    var refreshAfterGameEndCalled: Boolean = false
        private set

    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterStartNewGameCalled = false
        refreshAfterSwitchOneCalled = false
        refreshAfterSwitchAllCalled = false
        refreshAfterPassCalled = false
        refreshAfterTableDeckChangeCalled = false
        refreshAfterCloseCalled = false
        refreshAfterPlayerSwitchCalled = false
        refreshAfterGameEndCalled = false
    }

    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true
    }

    override fun refreshAfterSwitchOne(playerCard: Card, tableCard: Card) {
        refreshAfterSwitchOneCalled = true
    }

    override fun refreshAfterSwitchAll() {
        refreshAfterSwitchAllCalled = true
    }

    override fun refreshAfterPass() {
        refreshAfterPassCalled = true
    }

    override fun refreshAfterTableDeckChange() {
        refreshAfterTableDeckChangeCalled = true
    }

    override fun refreshAfterClose() {
        refreshAfterCloseCalled = true
    }

    override fun refreshAfterPlayerSwitch() {
        refreshAfterPlayerSwitchCalled = true
    }

    override fun refreshAfterGameEnd() {
        refreshAfterGameEndCalled = true
    }


}