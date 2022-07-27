package service

import entity.SwimGame

class RootService {

    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    var currentGame: SwimGame? = null
}