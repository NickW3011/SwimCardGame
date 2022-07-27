package entity

/**
 * This is an entity to represent a player in the game Swim. Every player has a name and 3 cards on
 * their hand.
 *
 * @param name unique name of the player
 * @param closed flag if the player has already done the close action
 * @param cardsOnHand the current 3 cars on the players hand
 */
data class Player(
    val name: String,
    val closed: Boolean = false,
    val cardsOnHand: Array<Card>
)