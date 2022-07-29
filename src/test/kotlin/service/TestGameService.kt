package service

import entity.*
import kotlin.test.*

/**
 * This is a class for testing the [GameService] functions
 */
class TestGameService {

    /**
     * Test function for starting a new [SwimGame]. It is checked weather the player size, number of cardsInMid, number
     * of both cardsOnHand, number of cards in the deck
     */
    @Test
    fun testStartNewGame() {
        val testRootService = RootService()
        val testPlayerCount = 2
        val testPlayers = arrayOf("testPlayer1", "testPlayer2")

        testRootService.gameService.startNewGame(testPlayerCount, testPlayers)

        val currentTestGame = requireNotNull(testRootService.currentGame)
        assertEquals(currentTestGame.players.size, testPlayerCount)
        assertEquals(currentTestGame.players[0].name, "testPlayer1")
        assertEquals(currentTestGame.players[1].name, "testPlayer2")
        assertEquals(currentTestGame.cardsInMid.size, 3)
        assertEquals(currentTestGame.players[0].cardsOnHand.size, 3)
        assertEquals(currentTestGame.players[1].cardsOnHand.size, 3)
        assertEquals(currentTestGame.deck.size, 23)
        assertNotEquals(currentTestGame.cardsInMid, currentTestGame.players[0].cardsOnHand)
        assertNotEquals(currentTestGame.cardsInMid, currentTestGame.players[1].cardsOnHand)
        assertNotEquals(currentTestGame.players[0].cardsOnHand, currentTestGame.players[1].cardsOnHand)
    }

    /**
     * Test function for counting the current points of a [Player]. It is checked if the correct points are returned for
     * the same three values and for different values
     */
    @Test
    fun testCountPoints() {
        val testRootService = RootService()

        val testCardsOnHandSameValue = arrayOf(
            Card(CardValue.TEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.DIAMONDS)
        )
        val testPlayerSameValue = Player("testPlayerSameValue", false, testCardsOnHandSameValue)

        val testCardsOnHandNotSameValue1 = arrayOf(
            Card(CardValue.ACE, CardSuit.CLUBS),
            Card(CardValue.EIGHT, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.DIAMONDS)
        )
        val testPlayerNotSameValue1 = Player("testPlayerNotSameValue1", false, testCardsOnHandNotSameValue1)

        val testCardsOnHandNotSameValue2 = arrayOf(
            Card(CardValue.ACE, CardSuit.CLUBS),
            Card(CardValue.KING, CardSuit.CLUBS),
            Card(CardValue.QUEEN, CardSuit.CLUBS)
        )
        val testPlayerNotSameValue2 = Player("testPlayerNotSameValue2", false, testCardsOnHandNotSameValue2)

        assertEquals(testRootService.gameService.countPoints(testPlayerSameValue), 30.5)
        assertEquals(testRootService.gameService.countPoints(testPlayerNotSameValue1), 19.0)
        assertEquals(testRootService.gameService.countPoints(testPlayerNotSameValue2), 31.0)
    }

    /**
     * Test function for checking if a game is over. Both cases are checked. The first case is, that every [Player] has
     * passed and the deck has less than 3 cards. The second case is, that the current [Player] has already closed. The
     * third case is, that the game is still running and more than 3 cards are in the deck
     */
    @Test
    fun testIsGameOver() {
        // First Case
        val testRootService1 = RootService()

        var testCardsOnHand1 = arrayOf(
            Card(CardValue.TEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.DIAMONDS)
        )
        var testPlayer1 = Player("testPlayer1", false, testCardsOnHand1)

        var testCardsOnHand2 = arrayOf(
            Card(CardValue.ACE, CardSuit.CLUBS),
            Card(CardValue.EIGHT, CardSuit.CLUBS),
            Card(CardValue.QUEEN, CardSuit.DIAMONDS)
        )
        var testPlayer2 = Player("testPlayer2", false, testCardsOnHand2)

        var testPlayers = arrayOf(testPlayer1, testPlayer2)

        var testCardsInMid = arrayOf(
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.ACE, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.SPADES)
        )

        var testDeck = mutableListOf(
            Card(CardValue.JACK, CardSuit.HEARTS),
            Card(CardValue.JACK, CardSuit.DIAMONDS)
        )

        var testGameAllPassed = SwimGame(2, 0, testCardsInMid, testDeck, testPlayers)
        testRootService1.currentGame = testGameAllPassed

        assert(testRootService1.gameService.isGameOver())

        // Second Case

        val testRootService2 = RootService()

        testCardsOnHand1 = arrayOf(
            Card(CardValue.TEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.DIAMONDS)
        )
        testPlayer1 = Player("testPlayer1", true, testCardsOnHand1)

        testCardsOnHand2 = arrayOf(
            Card(CardValue.ACE, CardSuit.CLUBS),
            Card(CardValue.EIGHT, CardSuit.CLUBS),
            Card(CardValue.QUEEN, CardSuit.DIAMONDS)
        )
        testPlayer2 = Player("testPlayer2", false, testCardsOnHand2)

        testPlayers = arrayOf(testPlayer1, testPlayer2)

        testCardsInMid = arrayOf(
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.ACE, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.SPADES)
        )

        testDeck = mutableListOf(
            Card(CardValue.JACK, CardSuit.HEARTS),
            Card(CardValue.JACK, CardSuit.DIAMONDS)
        )

        testGameAllPassed = SwimGame(0, 0, testCardsInMid, testDeck, testPlayers)
        testRootService2.currentGame = testGameAllPassed

        assert(testRootService2.gameService.isGameOver())

        // Third Case

        val testRootService3 = RootService()

        testCardsOnHand1 = arrayOf(
            Card(CardValue.TEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.HEARTS),
            Card(CardValue.TEN, CardSuit.DIAMONDS)
        )
        testPlayer1 = Player("testPlayer1", false, testCardsOnHand1)

        testCardsOnHand2 = arrayOf(
            Card(CardValue.ACE, CardSuit.CLUBS),
            Card(CardValue.EIGHT, CardSuit.CLUBS),
            Card(CardValue.QUEEN, CardSuit.DIAMONDS)
        )
        testPlayer2 = Player("testPlayer2", false, testCardsOnHand2)

        testPlayers = arrayOf(testPlayer1, testPlayer2)

        testCardsInMid = arrayOf(
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.ACE, CardSuit.DIAMONDS),
            Card(CardValue.ACE, CardSuit.SPADES)
        )

        testDeck = mutableListOf(
            Card(CardValue.JACK, CardSuit.HEARTS),
            Card(CardValue.JACK, CardSuit.DIAMONDS),
            Card(CardValue.JACK, CardSuit.SPADES),
            Card(CardValue.JACK, CardSuit.CLUBS)
        )

        testGameAllPassed = SwimGame(0, 0, testCardsInMid, testDeck, testPlayers)
        testRootService3.currentGame = testGameAllPassed

        assert(!testRootService3.gameService.isGameOver())
    }
}