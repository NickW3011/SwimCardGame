package entity

import kotlin.test.*

/**
 * Class to test the creation of the instance [SwimGame]
 */
class TestSwimGame {
    /**
     * Starting values for the Swim Game are set and the cardsInMid, deck and the players are created. [Player] 1 gets
     * cardsOnHand1 as his/her cards and [Player] 2 gets cardsOnHand2 as his/her cards. Since the array can`t be filled
     * with null, first they are given three of the same [Card] and then the stacks of [Card] are filled with helping
     * functions.
     */
    @Test
    fun createSwimGame() {
        val passedCounter = 0
        val currentPlayer = 1
        val cardsInMid = Array<Card>(3) { Card(CardValue.TWO, CardSuit.SPADES) }
        val mutableDeck = mutableListOf<Card>()
        val cardsOnHand1 = Array<Card>(3) { Card(CardValue.TWO, CardSuit.SPADES) }
        val cardsOnHand2 = Array<Card>(3) { Card(CardValue.TWO, CardSuit.SPADES) }

        fillCards(cardsInMid, mutableDeck, cardsOnHand1, cardsOnHand2)
        val deck = mutableDeck.toList()

        val player1 = Player("Foo", false, cardsOnHand1)
        val player2 = Player("Bar", false, cardsOnHand2)
        val players = arrayOf(player1, player2)

        val swimGame = SwimGame(
            passedCounter,
            currentPlayer,
            cardsInMid,
            deck,
            players
        )

        assertEquals(passedCounter, swimGame.passedCounter)
        assertEquals(currentPlayer, swimGame.currentPlayer)
        assertEquals(cardsInMid, swimGame.cardsInMid)
        assertEquals(deck, swimGame.deck)
        assertEquals(players, swimGame.players)
    }

    /**
     * Helping funcction to get a list with every possible [Card] in the game.
     */
    fun fillCardList(cards: MutableList<Card>) {
        enumValues<CardValue>().forEach { value ->
            val cardValue: CardValue = value
            enumValues<CardSuit>().forEach {
                val cardSuit: CardSuit = it
                cards.add(Card(cardValue, cardSuit))
            }
        }
    }

    /**
     * Helping funcction to fill the cardsInMid, deck, cardsOnHand1 and cardsOnHand2
     */
    fun fillCards(
        cardsInMid: Array<Card>, deck: MutableList<Card>,
        cardsOnHand1: Array<Card>, cardsOnHand2: Array<Card>
    ) {
        val cards = mutableListOf<Card>()
        fillCardList(cards)

        cardsInMid[0] = cards[0]
        cardsInMid[1] = cards[1]
        cardsInMid[2] = cards[2]

        cardsOnHand1[0] = cards[3]
        cardsOnHand1[1] = cards[4]
        cardsOnHand1[2] = cards[5]

        cardsOnHand2[0] = cards[6]
        cardsOnHand2[1] = cards[7]
        cardsOnHand2[2] = cards[8]

        for (i in 9..31) {
            deck.add(cards[i])
        }
    }
}