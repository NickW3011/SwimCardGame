package service

import entity.SwimGame
import view.Refreshable

/**
 * This is the [RootService] class, which holds the current [SwimGame] object and also provides two functions for adding
 * refreshable objects. It exists on top of the [GameService] and [PlayerActionService] classes and holds one instance
 * of each service class
 */
class RootService {

    /**
     * [GameService] instance
     */
    val gameService = GameService(this)

    /**
     * [PlayerActionService] instance
     */
    val playerActionService = PlayerActionService(this)

    /**
     * The current [SwimGame]. Can also be null if no game has been created yet
     */
    var currentGame: SwimGame? = null

    /**
     * This is a function for adding one [Refreshable] object to the [GameService] and the [PlayerActionService]
     *
     * @param newRefreshable [Refreshable] to add to both services
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * This is a function for adding multiple [Refreshable] objects to the [GameService] and the [PlayerActionService]
     *
     * @param newRefreshables [Refreshable] objects to add to both services
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}