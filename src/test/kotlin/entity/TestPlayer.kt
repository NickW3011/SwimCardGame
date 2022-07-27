package entity

import kotlin.test.*

/**
 * Class to test the creation of a [Player] in the Swim Game
 */
class TestPlayer {
    /**
     * Three [Card] objects are created and are put in an [Array]. Then a [Player] is created and it is checked if the
     * values of the created [Player] match the defined values
     */
    @Test
    fun createPlayer() {
        val card1 = Card(CardValue.ACE, CardSuit.CLUBS)
        val card2 = Card(CardValue.JACK, CardSuit.SPADES)
        val card3 = Card(CardValue.TWO, CardSuit.DIAMONDS)

        val name = "Nick"
        val closed = false
        val cardsOnHand: Array<Card> = arrayOf(card1, card2, card3)

        val createdPlayer = Player(name, closed, cardsOnHand)

        assertEquals(name, createdPlayer.name)
        assertEquals(closed, createdPlayer.closed)
        assertEquals(cardsOnHand, createdPlayer.cardsOnHand)
    }
}