package entity

/**
 * This is an entity to represent a card in the game Swim. Every card has a value and a suit.
 *
 * @param value card value
 * @param suit card suite
 */
data class Card(
    val value: CardValue,
    val suit: CardSuit
)