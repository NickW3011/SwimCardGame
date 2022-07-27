package service

import entity.Player

class PlayerActionService(
    private val rootService: RootService
) {

    private fun isPlayerActive(player: Player): Boolean {
        require(rootService.currentGame != null)
        val currentGame = rootService.currentGame!!
        val players = currentGame.players
        return players[currentGame.currentPlayer] == player
    }
}