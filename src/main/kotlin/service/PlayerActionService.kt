package service

import entity.Card
import entity.Player

/**
 * This is a service class which holds several functions for player actions as well as helping functions. It holds the
 * four player moves switchOne, switchAll, pass and close and also the two helping functions isPlayerActive and endMove.
 * It implements the [AbstractRefreshingService]
 *
 * @param rootService access to the [RootService] which holds the current game
 */
class PlayerActionService(
    private val rootService: RootService
) : AbstractRefreshingService() {

    /**
     * This is a private helping function to check weather a given [player] is active or not. It compares the given
     * [Player] to the currentPlayer given by the currentGame from the rootService
     *
     * @param player [Player] object to check
     * @return [Boolean] to determine if the player is active or not
     */
    private fun isPlayerActive(player: Player): Boolean {
        val currentGame = requireNotNull(rootService.currentGame)
        val players = currentGame.players
        return players[currentGame.currentPlayer] == player
    }

    /**
     * This is a private helping function to end a players move. It increases the currentPlayer or sets it to 1 depending
     * on the current value of currentPlayer
     *
     * @param player [Player] to end the move
     */
    private fun endMove(player: Player) {
        val currentGame = requireNotNull(rootService.currentGame)
        if (currentGame.currentPlayer == currentGame.players.size) {
            rootService.currentGame = currentGame.copy(currentPlayer = 1)
        } else {
            rootService.currentGame = currentGame.copy(currentPlayer = currentGame.currentPlayer + 1)
        }
    }


    /**
     * This is a function to switch one [Card] given by a [Player] with a [Card] from the table.
     *
     * @param player [Player] who wants to switch a [Card]
     * @param playerCard [Card] from the players hand
     * @param tableCard [Card] from the table
     */
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

    /**
     * This is a function to switch all cards from a [Player] with all cards on the table
     *
     * @param player [Player] to switch all cards
     */
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

    /**
     * This is a function to perform the [Player] move pass. If all players chose to pass, the three cardsInMid are
     * discarded and replaced with three new cards from the deck. If there are less than three cards available, the
     * game ends. The passedCounter is incremented by one or set to zero again if everyone passed
     *
     * @param player [Player] to pass
     */
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

    /**
     * This is a function for the [Player] move close. The attribute closed from [Player] is set to true and no card
     * changes are performed
     *
     * @param player [Player] to close
     */
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