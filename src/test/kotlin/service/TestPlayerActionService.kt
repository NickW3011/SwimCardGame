package service

import entity.*
import kotlin.test.*

/**
 * This is a class for testing the [PlayerActionService] functions
 */
class TestPlayerActionService {

    /**
     * Helping function for creating a test [SwimGame] and test [RootService]
     */
    private fun createTestRootService(): RootService {
        val rootService = RootService()
        val gameCards = mutableListOf<Card>()
        fillCardList(gameCards)

        val player1CardsOnHand = arrayOf(gameCards[0], gameCards[1], gameCards[2])
        val player1 = Player("testPlayer1", false, player1CardsOnHand)

        val player2CardsOnHand = arrayOf(gameCards[3], gameCards[4], gameCards[5])
        val player2 = Player("testPlayer2", false, player2CardsOnHand)

        val players = arrayOf(player1, player2)

        val cardsInMid = arrayOf(gameCards[6], gameCards[7], gameCards[8])

        val deck = gameCards.subList(9, gameCards.size - 1).toList()

        val testGame = SwimGame(0, 0, cardsInMid, deck, players)

        rootService.currentGame = testGame
        return rootService
    }

    /**
     * Helping function to get a list with every possible [Card] in the game
     *
     * @param cards [MutableList] to be filled with all possible cards
     */
    private fun fillCardList(cards: MutableList<Card>) {
        enumValues<CardValue>().forEach { value ->
            val cardValue: CardValue = value
            enumValues<CardSuit>().forEach {
                val cardSuit: CardSuit = it
                cards.add(Card(cardValue, cardSuit))
            }
        }
    }

    /**
     * Function for testing if a [PlayerActionService] instance is created properly when creating a [RootService] instance
     */
    @Test
    fun testCreatePlayerActionService() {
        val rootService = RootService()
        assertNotNull(rootService.playerActionService)
    }

    /**
     * Function for testing the switchOne function from [PlayerActionService]. It is checked weather the two selected
     * cards are switched properly
     */
    @Test
    fun testSwitchOne() {
        val testRootService = createTestRootService()
        val currentTestGame = requireNotNull(testRootService.currentGame)
        val testPlayer = currentTestGame.players[currentTestGame.currentPlayer]
        val testCardOnHand = currentTestGame.players[currentTestGame.currentPlayer].cardsOnHand[1]
        val tesCardInMid = currentTestGame.cardsInMid[1]

        testRootService.playerActionService.switchOne(testPlayer, testCardOnHand, tesCardInMid)

        val newCurrentTestGame = requireNotNull(testRootService.currentGame)
        assertEquals(newCurrentTestGame.cardsInMid[1], testCardOnHand)
        assertEquals(newCurrentTestGame.players[newCurrentTestGame.currentPlayer - 1].cardsOnHand[1], tesCardInMid)
    }

    /**
     * Function for testing the switchAll function in [PlayerActionService]. It is checked if the new player hand matches
     * the old cardsInMid and vice versa
     */
    @Test
    fun testSwitchAll() {
        val testRootService = createTestRootService()
        val currentTestGame = requireNotNull(testRootService.currentGame)
        val testPlayer = currentTestGame.players[currentTestGame.currentPlayer]
        val cardsOnHand = testPlayer.cardsOnHand
        val cardsInMid = currentTestGame.cardsInMid

        testRootService.playerActionService.switchAll(testPlayer)

        val newCurrentTestGame = requireNotNull(testRootService.currentGame)
        assertEquals(newCurrentTestGame.cardsInMid, cardsOnHand)
        assertEquals(newCurrentTestGame.players[newCurrentTestGame.currentPlayer - 1].cardsOnHand, cardsInMid)
    }

    /**
     * Function for testing the pass function in [PlayerActionService]. It is checked if the cardsInMid are correct if
     * not every [Player] has passed and all players have passed. It also is checked that the deck size is correct after
     * a pass with not enough card in the deck
     */
    @Test
    fun testPass() {
        val testRootService = createTestRootService()
        var currentTestGame = requireNotNull(testRootService.currentGame)
        val cardsInMid = currentTestGame.cardsInMid
        val testPlayer = currentTestGame.players[currentTestGame.currentPlayer]

        testRootService.playerActionService.pass(testPlayer)

        val currentTestGameAfterFirstPass = requireNotNull(testRootService.currentGame)
        assertEquals(cardsInMid, currentTestGameAfterFirstPass.cardsInMid)
        assertEquals(currentTestGameAfterFirstPass.passedCounter, 1)

        testRootService.playerActionService.pass(currentTestGameAfterFirstPass.players[currentTestGameAfterFirstPass.currentPlayer])

        val currentTestGameAfterSecondPass = requireNotNull(testRootService.currentGame)
        assertNotEquals(cardsInMid, currentTestGameAfterSecondPass.cardsInMid)
        assertEquals(currentTestGameAfterSecondPass.passedCounter, 0)

        for (i in 1..12) {
            currentTestGame = requireNotNull(testRootService.currentGame)
            testRootService.playerActionService.pass(currentTestGame.players[currentTestGame.currentPlayer])
        }

        currentTestGame = requireNotNull(testRootService.currentGame)
        assert(currentTestGame.deck.size < 3)
    }

    /**
     * Function for testing the close function in [PlayerActionService]. It is checked if the closed flag is set to true
     * after a close action
     */
    @Test
    fun testClose() {
        val testRootService = createTestRootService()
        var currentTestGame = requireNotNull(testRootService.currentGame)
        val testPlayer = currentTestGame.players[currentTestGame.currentPlayer]

        testRootService.playerActionService.close(testPlayer)

        currentTestGame = requireNotNull(testRootService.currentGame)
        assertNotEquals(currentTestGame.players[currentTestGame.currentPlayer - 1].closed, testPlayer.closed)
    }
}