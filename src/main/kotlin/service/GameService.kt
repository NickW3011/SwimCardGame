package service

import entity.*
import kotlin.math.max
import kotlin.random.Random

/**
 * This is a service class which holds several functions for game actions as well as helping functions. It holds the
 * game actions startNewGame, countPoints and isGameOver and also several helping functions. It implements the
 * [AbstractRefreshingService]
 *
 * @param rootService access to the [RootService] which holds the current game
 */
class GameService(
    private val rootService: RootService
) : AbstractRefreshingService() {

    // Function startNewGame

    /**
     * Function to create a new [SwimGame] and set the currentGame in [RootService] to the new game. The cardsInMid and
     * the players are created using helping functions. The passedCounter starts at 0 and the currentPlayer at 1. After
     * creating the players with their starting carsOnHand and setting the cardsInMid, the unused cards are put in a
     * random order in the deck
     *
     * @param playerCount number of players
     * @param players [Array] with names of players
     */
    fun startNewGame(playerCount: Int, players: Array<String>) {
        require(players.distinct().toTypedArray().contentEquals(players))
        val availableCards = mutableListOf<Card>()
        fillCardList(availableCards)

        val passedCounter = 0
        val currentPlayer = 0
        val cardsInMid = getRandomThreeCards(availableCards)
        val swimPlayers = createPlayers(playerCount, availableCards, players)
        val deck = randomizeCards(availableCards).toList()

        rootService.currentGame = SwimGame(passedCounter, currentPlayer, cardsInMid, deck, swimPlayers)

        //refreshAfterStartNewGame()
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
     * Helping function to get 3 [Card] objects in a random order
     *
     * @param availableCards cards to get three random cards from
     * @return [Array] of the randomly chosen cards
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
     *
     * @param playerCount number of players to create
     * @param availableCards cards to generate the starting hand for the players from
     * @param names names of the players
     * @return [Array] of [Player] objects with randomly set cardsOnHand
     */
    private fun createPlayers(
        playerCount: Int,
        availableCards: MutableList<Card>,
        names: Array<String>
    ): Array<Player> {
        val players = arrayOfNulls<Player>(playerCount)
        for (i in 0 until playerCount) {
            val cardsOnHand = getRandomThreeCards(availableCards)
            val player = Player(names[i], false, cardsOnHand)
            players[i] = player
        }
        return players.requireNoNulls()
    }

    /**
     * Helping function to randomize the order of [Card] objects
     *
     * @param availableCards [MutableList] of cards to randomize
     * @return [MutableList] of randomized cards
     */
    private fun randomizeCards(availableCards: MutableList<Card>): MutableList<Card> {
        val randomizedCards = mutableListOf<Card>()
        for (i in 0 until availableCards.size) {
            var randomInt = 0
            if (availableCards.size - 1 != 0) {
                randomInt = Random.nextInt(0, availableCards.size - 1)
            }
            randomizedCards.add(availableCards[randomInt])
            availableCards.removeAt(randomInt)
        }
        return randomizedCards
    }

    // Function countPoints

    /**
     * Function to count the current points of a [Player] by the rules of the Swim Game. If a [Player] has 3 cards with
     * the same value, the [Player] gets 30.5 points, if that is not the case, the highest sum of one suit is returned
     *
     * @param player [Player] to count the points from
     * @return player points as [Double]
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
     *
     * @param suit [CardSuit] to count points from
     * @param cards [Array] of cards to count points from
     * @return points of suit as [Double]
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
     *
     * @param cardValue [CardValue] to convert to [Double]
     * @return value of [CardValue] as [Double]
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

    // Function isGameOver

    /**
     * Function for checking if the current game has finished. A game is over if every player has passed or if the
     * current player has already closed the game
     *
     * @return [Boolean] weather the current [SwimGame] is over
     */
    fun isGameOver(): Boolean {
        return (requireNotNull(rootService.currentGame).passedCounter ==
                requireNotNull(rootService.currentGame).players.size &&
                requireNotNull(rootService.currentGame).deck.size < 3) ||
                requireNotNull(rootService.currentGame).players[
                        requireNotNull(rootService.currentGame).currentPlayer
                ].closed
    }
}