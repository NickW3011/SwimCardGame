package service

import entity.SwimGame
import view.Refreshable

class RootService {

    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    var currentGame: SwimGame? = null

    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}