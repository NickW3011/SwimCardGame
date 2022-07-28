package service

import entity.Card
import entity.Player

class PlayerActionService(
    private val rootService: RootService
) : AbstractRefreshingService() {

    private fun isPlayerActive(player: Player): Boolean {
        val currentGame = requireNotNull(rootService.currentGame)
        val players = currentGame.players
        return players[currentGame.currentPlayer] == player
    }

    private fun endMove(player: Player) {
        val currentGame = requireNotNull(rootService.currentGame)
        if (currentGame.currentPlayer == currentGame.players.size) {
            rootService.currentGame = currentGame.copy(currentPlayer = 1)
        } else {
            rootService.currentGame = currentGame.copy(currentPlayer = currentGame.currentPlayer + 1)
        }
    }

    fun switchOne(player: Player, playerCard: Card, tableCard: Card) {
        val currentGame = requireNotNull(rootService.currentGame)
        require(isPlayerActive(player))
        require(player.cardsOnHand.contains(playerCard))
        require(currentGame.cardsInMid.contains(tableCard))

        val newPassedCounter = 0

        val newCardsOnHand = player.cardsOnHand
        newCardsOnHand[newCardsOnHand.indexOf(playerCard)] = tableCard

        val newCardsInMid = currentGame.cardsInMid
        newCardsInMid[newCardsInMid.indexOf(tableCard)] = playerCard

        val newPlayer = player.copy(cardsOnHand = newCardsOnHand)

        val newPlayers = currentGame.players
        newPlayers[newPlayers.indexOf(player)] = newPlayer

        val newSwimGame =
            currentGame.copy(passedCounter = newPassedCounter, cardsInMid = newCardsInMid, players = newPlayers)
        rootService.currentGame = newSwimGame

        onAllRefreshables { refreshAfterSwitchOne(playerCard, tableCard) }
        endMove(player)
    }

    fun switchAll(player: Player) {
        val currentGame = requireNotNull(rootService.currentGame)
        require(isPlayerActive(player))

        val newPassedCounter = 0

        val newPlayer = player.copy(cardsOnHand = currentGame.cardsInMid)

        val newPlayers = currentGame.players
        newPlayers[newPlayers.indexOf(player)] = newPlayer

        val newSwimGame =
            currentGame.copy(passedCounter = newPassedCounter, cardsInMid = player.cardsOnHand, players = newPlayers)
        rootService.currentGame = newSwimGame

        onAllRefreshables { refreshAfterSwitchAll() }
        endMove(player)
    }

    fun pass(player: Player) {
        val currentGame = requireNotNull(rootService.currentGame)
        require(isPlayerActive(player))

        var newPassedCounter = currentGame.passedCounter + 1

        if (newPassedCounter == currentGame.players.size) {
            if (currentGame.deck.size < 3) {
                onAllRefreshables { refreshAfterGameEnd() }
                return
            }
            val newCardsInMid = currentGame.deck.subList(0, 2)
            rootService.currentGame = currentGame.copy(
                passedCounter = newPassedCounter,
                cardsInMid = newCardsInMid.toTypedArray()
            )

            onAllRefreshables { refreshAfterTableDeckChange() }
            newPassedCounter = 0
        }

        rootService.currentGame = currentGame.copy(passedCounter = newPassedCounter)

        onAllRefreshables { refreshAfterPass() }
        endMove(player)
    }

    fun close(player: Player) {
        val currentGame = requireNotNull(rootService.currentGame)
        require(isPlayerActive(player))

        val newPlayer = player.copy(closed = true)
        val newPlayers = currentGame.players
        newPlayers[newPlayers.indexOf(player)] = newPlayer

        val newSwimGame = currentGame.copy(players = newPlayers)
        rootService.currentGame = newSwimGame

        onAllRefreshables { refreshAfterClose() }
        endMove(player)
    }
}