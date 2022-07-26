package entity

/**
 * This is an entity to represent the current Swim Game. It holds current game information like the
 * current player(s), the current cards and a counter for the pass action.
 *
 * @param passedCounter counts how many players have done the action pass in a row
 * @param currentPlayer the number of the current player
 * @param cardsInMid the 3 cars in the middle
 * @param deck the cards that can be drawn
 * @param players the 2-4 players currently playing the game
 */
data class SwimGame(
    var passedCounter: Int,
    var currentPlayer: Int,
    var cardsInMid: Array<Card>,
    var deck: List<Card>,
    val players: Array<Player>
)
