package view

import entity.Card
import entity.Player

/**
 * This is an interface for several refreshing function at several points in time in a Swim Game
 */
interface Refreshable {

    /**
     * Refreshing function after a new game was started
     */
    fun refreshAfterStartNewGame()

    /**
     * Refreshing function after one [Card] was switched
     *
     * @param playerCard [Card] on [Player] hand to be switched
     * @param tableCard [Card] on table to be switched
     */
    fun refreshAfterSwitchOne(playerCard: Card, tableCard: Card)

    /**
     * Refreshing function after all [Player] cards are switched
     */
    fun refreshAfterSwitchAll()

    /**
     * Refreshing function after a [Player] decides to pass
     */
    fun refreshAfterPass()

    /**
     * Refreshing function after the deck on the table changed
     */
    fun refreshAfterTableDeckChange()

    /**
     * Refreshing function after a [Player] decides to close
     */
    fun refreshAfterClose()

    /**
     * Refreshing function after the current [Player] is switched
     */
    fun refreshAfterPlayerSwitch()

    /**
     * Refreshing function after the Swim Game is over
     */
    fun refreshAfterGameEnd()
}