package service

import entity.*
import kotlin.math.max
import kotlin.random.Random

class GameService(
    private val rootService: RootService
) {

    // Function startNewGame

    /**
     * Function to create a new [SwimGame] and set the currentGame in [RootService] to the new game. The cardsInMid and
     * the players are created using helping functions. The passedCounter starts at 0 and the currentPlayer at 1. After
     * creating the players with their starting carsOnHand and setting the cardsInMid, the unused cards are put in a
     * random order in the deck
     */
    fun startNewGame(playerCount: Int, players: Array<String>) {
        val availableCards = mutableListOf<Card>()
        fillCardList(availableCards)

        val passedCounter = 0
        val currentPlayer = 1
        val cardsInMid = getRandomThreeCards(availableCards)
        val swimPlayers = createPlayers(playerCount, availableCards, players)
        val deck = randomizeCards(availableCards).toList()

        rootService.currentGame = SwimGame(passedCounter, currentPlayer, cardsInMid, deck, swimPlayers)
    }

    /**
     * Helping function to get a list with every possible [Card] in the game
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
     * Helping function to get 3 [Card] objects in a random order
     */
    private fun getRandomThreeCards(availableCards: MutableList<Card>): Array<Card> {
        val cards = arrayOfNulls<Card>(3)
        for (i in 0..2) {
            val randomInt = Random.nextInt(0, availableCards.size - 1)
            cards[i] = availableCards[randomInt]
            availableCards.removeAt(randomInt)
        }
        return cards.requireNoNulls()
    }

    /**
     * Helping function to create the new [Player] objects. For each [Player], 3 random [Card] objects are generated
     * and put in their hand
     */
    private fun createPlayers(playerCount: Int,
                              availableCards: MutableList<Card>,
                              names: Array<String>): Array<Player> {
        val players = arrayOfNulls<Player>(playerCount)
        for (i in 0..playerCount) {
            val cardsOnHand = getRandomThreeCards(availableCards)
            val player = Player(names[i], false, cardsOnHand)
            players[i] = player
        }
        return players.requireNoNulls()
    }

    /**
     * Helping function to randomize the order of [Card] objects
     */
    private fun randomizeCards(availableCards: MutableList<Card>): MutableList<Card> {
        val randomizedCards = mutableListOf<Card>()
        for (i in 0 until availableCards.size - 1) {
            val randomInt = Random.nextInt(0, availableCards.size - 1)
            randomizedCards.add(availableCards[randomInt])
            availableCards.removeAt(randomInt)
        }
        return randomizedCards
    }

    // Function countPoints

    /**
     * Function to count the current points of a [Player] by the rules of the Swim Game. If a [Player] has 3 cards with
     * the same value, the [Player] gets 30.5 points, if that is not the case, the highest sum of one suit is returned
     */
    fun countPoints(player: Player): Double {
        val cards = player.cardsOnHand

        //check if cards have the same value
        if (cards[0].value == cards[1].value && cards[1].value == cards[2].value) {
            return 30.5
        }
        return max(
            max(countPointsOfSuit(CardSuit.SPADES, cards), countPointsOfSuit(CardSuit.HEARTS, cards)),
            max(countPointsOfSuit(CardSuit.DIAMONDS, cards), countPointsOfSuit(CardSuit.CLUBS, cards))
        )
    }

    /**
     * Helping function to count the points of one [CardSuit]
     */
    private fun countPointsOfSuit(suit: CardSuit, cards: Array<Card>): Double {
        var points = 0.0
        for (card in cards) {
            if (card.suit == suit) {
                points += cardValueToDouble(card.value)
            }
        }
        return points
    }

    /**
     * Helping function to convert a [CardValue] to a [Double]. ACE is converted to 11 points and QUEEN, KING and JACK
     * are converted to 10 points. The other values are converted to the number they represent
     */
    private fun cardValueToDouble(cardValue: CardValue): Double {
        return if (cardValue == CardValue.ACE) {
            11.0
        } else if (cardValue == CardValue.QUEEN || cardValue == CardValue.KING || cardValue == CardValue.JACK) {
            10.0
        } else {
            Integer.parseInt(cardValue.toString()).toDouble()
        }
    }
}