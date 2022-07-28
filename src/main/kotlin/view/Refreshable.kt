package view

import entity.Card

interface Refreshable {

    fun refreshAfterStartNewGame()

    fun refreshAfterSwitchOne(playerCard: Card, tableCard: Card)

    fun refreshAfterSwitchAll()

    fun refreshAfterPass()

    fun refreshAfterTableDeckChange()

    fun refreshAfterClose()

    fun refreshAfterPlayerSwitch()

    fun refreshAfterGameEnd()
}