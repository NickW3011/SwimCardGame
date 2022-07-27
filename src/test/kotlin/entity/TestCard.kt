package entity

import kotlin.test.*

/**
 * Class to test the creation of a [Card] in the Swim Game
 */
class TestCard {
    /**
     * A [Card] is created with the value ACE and the suit HEARTS and it is checked if the values of the created [Card]
     * match the defined values
     */
    @Test
    fun createCard() {
        val cardValue: CardValue = CardValue.ACE
        val cardSuit: CardSuit = CardSuit.HEARTS

        val createdCard = Card(cardValue, cardSuit)

        assertEquals(cardValue, createdCard.value)
        assertEquals(cardSuit, createdCard.suit)
    }
}