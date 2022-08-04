package view

import entity.Card
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * This class represents the current game scene. It holds the player cards, the cardsInMid, a score label and other game
 * components
 */
class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

    private var highlightedHandCard: CardView? = null
    private var highlightedTableCard: CardView? = null

    private fun setCardIndex(card: CardView, index: Int, isMiddleCard: Boolean) {
        card.apply {
            onMouseClicked = {
                val currentGame = requireNotNull(rootService.currentGame)
                if (currentSide == CardView.CardSide.FRONT && switchOnePressed) {
                    if (chosenHandCard != null && isMiddleCard) {
                        rootService.playerActionService.switchOne(
                            currentGame.players[currentGame.currentPlayer],
                            requireNotNull(chosenHandCard),
                            currentGame.cardsInMid[index]
                        )
                        highlightedHandCard = null
                        highlightedTableCard = null
                    } else if (chosenTableCard != null && !isMiddleCard) {
                        rootService.playerActionService.switchOne(
                            currentGame.players[currentGame.currentPlayer],
                            currentGame.players[currentGame.currentPlayer].cardsOnHand[index],
                            requireNotNull(chosenTableCard)
                        )
                        highlightedHandCard = null
                        highlightedTableCard = null
                    } else {
                        if (isMiddleCard) {
                            chosenTableCard = currentGame.cardsInMid[index]
                            if (highlightedTableCard != null) {
                                playAnimation(
                                    MovementAnimation(
                                        componentView = highlightedTableCard!!,
                                        byX = 0, byY = 0, duration = 0
                                    )
                                )
                                playAnimation(
                                    MovementAnimation(componentView = this, byX = 0, byY = -25, duration = 200)
                                )
                            } else {
                                playAnimation(
                                    MovementAnimation(componentView = this, byX = 0, byY = -25, duration = 200)
                                )
                            }
                            highlightedTableCard = this
                        } else {
                            chosenHandCard = currentGame.players[currentGame.currentPlayer].cardsOnHand[index]
                            if (highlightedHandCard != null) {
                                playAnimation(
                                    MovementAnimation(
                                        componentView = highlightedHandCard!!,
                                        byX = 0, byY = 0, duration = 0
                                    )
                                )
                                playAnimation(
                                    MovementAnimation(componentView = this, byX = 0, byY = -25, duration = 200)
                                )
                            } else {
                                playAnimation(
                                    MovementAnimation(componentView = this, byX = 0, byY = -25, duration = 200)
                                )
                            }
                            highlightedHandCard = this
                        }
                    }
                }
            }
        }
    }

    private fun createCardViews(cards: Array<Card>, haveRotation: Boolean): List<CardView> {

        val cardImageLoader = CardImageLoader()

        val card1: CardView
        val card2: CardView
        val card3: CardView

        if (haveRotation) {
            card1 = CardView(
                posX = 0, posY = 20, width = cardWidth, height = cardHeight,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[0].suit, cards[0].value)),
                back = ImageVisual(cardImageLoader.backImage),
            ).apply {
                rotation = -15.0
            }

            card2 = CardView(
                posX = card1.posX + card1.width + 30, posY = 0, width = cardWidth, height = cardHeight,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[1].suit, cards[1].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )

            card3 = CardView(
                posX = card2.posX + card2.width + 30, posY = 20, width = cardWidth, height = cardHeight,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[2].suit, cards[2].value)),
                back = ImageVisual(cardImageLoader.backImage)
            ).apply {
                rotation = 15.0
            }
        } else {
            card1 = CardView(
                posX = 0, posY = 0, width = cardWidth, height = cardHeight,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[0].suit, cards[0].value)),
                back = ImageVisual(cardImageLoader.backImage),
            )

            card2 = CardView(
                posX = card1.posX + card1.width + 30, posY = 0, width = cardWidth, height = cardHeight,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[1].suit, cards[1].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )

            card3 = CardView(
                posX = card2.posX + card2.width + 30, posY = 0, width = cardWidth, height = cardHeight,
                front = ImageVisual(cardImageLoader.frontImageFor(cards[2].suit, cards[2].value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
        }

        if (haveRotation) {
            setCardIndex(card1, 0, false)
            setCardIndex(card2, 1, false)
            setCardIndex(card3, 2, false)
        } else {
            setCardIndex(card1, 0, true)
            setCardIndex(card2, 1, true)
            setCardIndex(card3, 2, true)
        }


        return listOf(card1, card2, card3)
    }

    private val cardWidth = 130
    private val cardHeight = 200

    private var chosenHandCard: Card? = null
    private var chosenTableCard: Card? = null

    private val cardPaneBottom: Pane<CardView> = Pane(
        width = 450, height = 150, posX = (this.width / 2) - 225, posY = 675
    )

    private val cardPaneTop: Pane<CardView> = Pane<CardView>(
        width = 450, height = 150, posX = (this.width / 2) - 225, posY = 0,
    ).apply {
        rotation = 180.0
    }

    private val topNameLabel = Label(
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = 175,
        text = "",
        font = Font(size = 40)
    )

    private val cardPaneLeft: Pane<CardView> = Pane<CardView>(
        width = 450, height = 150, posX = -150, posY = (this.height / 2) - 75
    ).apply {
        rotation = 90.0
    }

    private val leftNameLabel = Label(
        width = 450, height = 50, posX = 0, posY = (this.height / 2) - 25,
        text = "",
        font = Font(size = 40)
    ).apply {
        rotation = 90.0
    }

    private val cardPaneRight: Pane<CardView> = Pane<CardView>(
        width = 450, height = 150, posX = this.width - 300, posY = (this.height / 2) - 75,
    ).apply {
        rotation = -90.0
    }

    private val rightNameLabel = Label(
        width = 450, height = 50, posX = this.width - 450, posY = (this.height / 2) - 25,
        text = "",
        font = Font(size = 40)
    ).apply {
        rotation = -90.0
    }

    private val cardPaneMiddle: Pane<CardView> = Pane(
        width = 450, height = 150, posX = (this.width / 2) - 315, posY = (this.height / 2) - 200,
    )

    private val deckCard: CardView = CardView(
        posX = cardPaneMiddle.posX + cardPaneMiddle.width + 50,
        posY = cardPaneMiddle.posY,
        width = cardWidth, height = cardHeight,
        front = ColorVisual(0, 0, 0)
    ).apply {
        val cardImageLoader = CardImageLoader()
        backVisual = ImageVisual(cardImageLoader.backImage)
    }

    private val revealButton = Button(
        posX = cardPaneBottom.posX + (cardPaneBottom.width / 2) - 160,
        posY = cardPaneBottom.posY + (cardPaneBottom.height / 2) - 20,
        height = 75, width = 320,
        text = "Reveal Cards",
        font = Font(size = 40),
        visual = Visual.EMPTY
    ).apply {
        onMouseClicked = {
            revealCards()
        }
        componentStyle = """
            -fx-border-color: #B85450;
            -fx-background-color: #f8cecc;
            -fx-border-width: 5px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
                """.trimIndent()
    }

    private val middleNameLabel = Label(
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = (this.height / 2) + 25,
        text = "",
        font = Font(size = 40, color = Color.BLUE)
    )

    private val middleLabel = Label(
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = middleNameLabel.posY + 50,
        text = "It`s your turn",
        font = Font(size = 20, fontStyle = Font.FontStyle.ITALIC, color = Color.BLUE)
    )

    private val actionButtonWidth = 300
    private val actionButtonFontSize = 30

    private var switchOnePressed = false

    private val switchOneButton = Button(
        width = actionButtonWidth, height = 100, posX = 0, posY = 0,
        text = "Switch One Card",
        font = Font(size = actionButtonFontSize),
        visual = Visual.EMPTY
    ).apply {
        onMouseClicked = {
            switchOnePressed = true
            switchAllButton.isDisabled = true
            passButton.isDisabled = true
            closeButton.isDisabled = true
        }
        componentStyle = """
            -fx-border-color: #D6B656;
            -fx-border-width: 5px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
            -fx-background-color: #FFF2CC;
            -fx-text-alignment: CENTER;
                """.trimIndent()
    }

    private val switchAllButton: Button = Button(
        width = actionButtonWidth, height = 100, posX = switchOneButton.posX + switchOneButton.width + 50, posY = 0,
        text = "Switch All Cards",
        font = Font(size = actionButtonFontSize),
        visual = Visual.EMPTY
    ).apply {
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            rootService.playerActionService.switchAll(currentGame.players[currentGame.currentPlayer])
        }
        componentStyle = """
            -fx-border-color: #D79B00;
            -fx-border-width: 5px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
            -fx-background-color: #FFE6CC;
            -fx-text-alignment: CENTER;
                """.trimIndent()
    }

    private val passButton = Button(
        width = actionButtonWidth, height = 100, posX = switchAllButton.posX + switchAllButton.width + 50, posY = 0,
        text = "Pass",
        font = Font(size = actionButtonFontSize),
        visual = Visual.EMPTY
    ).apply {
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            rootService.playerActionService.pass(currentGame.players[currentGame.currentPlayer])
        }
        componentStyle = """
            -fx-border-color: #666666;
            -fx-border-width: 5px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
            -fx-background-color: #F5F5F5;
                """.trimIndent()
    }

    private val passRoundButtonWidth = 30
    private val passRoundButtonHeight = 20

    /*private val passRoundButtonActiveBackground = "f2de83"
    private val passRoundButtonActiveBorder = "a38c45"

    private val passRoundButtonPassiveBackground = "F5F5F5"
    private val passRoundButtonPassiveBorder = "666666"*/

    private val passRoundButton1 = Button(
        width = passRoundButtonWidth, height = passRoundButtonHeight,
        posX = switchAllButton.posX + switchAllButton.width + 60, posY = 60,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = """
            -fx-border-color: #666666;
            -fx-border-width: 2px;
            -fx-border-radius: 50%;
            -fx-background-radius: 50%;
            -fx-background-color: #F5F5F5;
                """.trimIndent()
    }

    private val passRoundButton2 = Button(
        width = passRoundButtonWidth, height = passRoundButtonHeight,
        posX = passRoundButton1.posX + passRoundButton1.width + 5, posY = 60,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = """
            -fx-border-color: #666666;
            -fx-border-width: 2px;
            -fx-border-radius: 50%;
            -fx-background-radius: 50%;
            -fx-background-color: #F5F5F5;
                """.trimIndent()
    }

    private val passRoundButton3 = Button(
        width = passRoundButtonWidth, height = passRoundButtonHeight,
        posX = passRoundButton2.posX + passRoundButton2.width + 5, posY = 60,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = """
            -fx-border-color: #666666;
            -fx-border-width: 2px;
            -fx-border-radius: 50%;
            -fx-background-radius: 50%;
            -fx-background-color: #F5F5F5;
                """.trimIndent()
    }

    private val passRoundButton4 = Button(
        width = passRoundButtonWidth, height = passRoundButtonHeight,
        posX = passRoundButton3.posX + passRoundButton3.width + 5, posY = 60,
        visual = Visual.EMPTY
    ).apply {
        componentStyle = """
            -fx-border-color: #666666;
            -fx-border-width: 2px;
            -fx-border-radius: 50%;
            -fx-background-radius: 50%;
            -fx-background-color: #F5F5F5;
                """.trimIndent()
    }

    private val passRoundButtons = arrayOf(passRoundButton1, passRoundButton2, passRoundButton3, passRoundButton4)

    private val closeButton = Button(
        width = actionButtonWidth, height = 100, posX = passButton.posX + passButton.width + 50, posY = 0,
        text = "Close",
        font = Font(size = actionButtonFontSize),
        visual = Visual.EMPTY
    ).apply {
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            rootService.playerActionService.close(currentGame.players[currentGame.currentPlayer])
        }
        componentStyle = """
            -fx-border-color: #B85450;
            -fx-background-color: #F8CECC;
            -fx-border-width: 5px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
                """.trimIndent()
    }

    private val buttonPane: Pane<Button> = Pane<Button>(
        width = 1250, height = 100,
        posX = (this.width / 2) - 675, posY = cardPaneBottom.posY + 275,
    ).apply {
        addAll(
            listOf(
                switchOneButton,
                switchAllButton,
                passButton,
                closeButton,
                passRoundButton1,
                passRoundButton2,
                passRoundButton3,
                passRoundButton4
            )
        )
    }

    private val scoreLabel = Label(
        width = 450, height = 50, posX = -100, posY = this.height - 150,
        text = "Score",
        font = Font(size = 40)
    )

    private val score = Label(
        width = 450, height = 50,
        posX = scoreLabel.posX + (scoreLabel.width / 2) - 225, posY = scoreLabel.posY + scoreLabel.height,
        text = "?",
        font = Font(size = 60, fontWeight = Font.FontWeight.BOLD)
    )

    init {
        opacity = 1.0
        background = ColorVisual(1, 155, 33)
        addComponents(
            cardPaneBottom,
            cardPaneTop,
            cardPaneLeft,
            cardPaneRight,
            topNameLabel,
            leftNameLabel,
            rightNameLabel,
            cardPaneMiddle,
            deckCard,
            revealButton,
            middleNameLabel,
            middleLabel,
            buttonPane,
            scoreLabel,
            score
        )
    }

    /**
     * This function creates the [CardView] objects and sets the player cards and middle cards
     */
    fun initCardPanes() {
        val currentGame = requireNotNull(rootService.currentGame)
        when (currentGame.players.size) {
            2 -> {
                cardPaneLeft.isVisible = false
                cardPaneRight.isVisible = false
                leftNameLabel.isVisible = false
                rightNameLabel.isVisible = false
                topNameLabel.text = currentGame.players[currentGame.currentPlayer + 1].name
                cardPaneTop.addAll(createCardViews(currentGame.players[1].cardsOnHand, true))
                passRoundButton3.isVisible = false
                passRoundButton4.isVisible = false
            }
            3 -> {
                cardPaneTop.isVisible = false
                topNameLabel.isVisible = false
                leftNameLabel.text = currentGame.players[2].name
                cardPaneLeft.addAll(createCardViews(currentGame.players[2].cardsOnHand, true))
                rightNameLabel.text = currentGame.players[1].name
                cardPaneRight.addAll(createCardViews(currentGame.players[1].cardsOnHand, true))
                passRoundButton4.isVisible = false
            }
            4 -> {
                leftNameLabel.text = currentGame.players[3].name
                cardPaneLeft.addAll(createCardViews(currentGame.players[3].cardsOnHand, true))
                rightNameLabel.text = currentGame.players[1].name
                cardPaneRight.addAll(createCardViews(currentGame.players[1].cardsOnHand, true))
                topNameLabel.text = currentGame.players[2].name
                cardPaneTop.addAll(createCardViews(currentGame.players[2].cardsOnHand, true))
            }
        }

        cardPaneBottom.addAll(createCardViews(currentGame.players[0].cardsOnHand, true))

        val cardsInMid = currentGame.cardsInMid
        cardPaneMiddle.addAll(createCardViews(cardsInMid, false))
        cardPaneMiddle.onEach {
            it.showFront()
        }

        middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
    }

    private fun revealCards() {
        cardPaneBottom.onEach {
            playAnimation(
                FlipAnimation(
                    componentView = it,
                    fromVisual = it.backVisual,
                    toVisual = it.frontVisual,
                    duration = 400
                )
            )
            it.showFront()
        }
        revealButton.isVisible = false
        score.apply {
            val currentGame = requireNotNull(rootService.currentGame)
            text = rootService.gameService.countPoints(currentGame.players[currentGame.currentPlayer]).toString()
        }
    }

    private var nextPlayerSwitchDelayed = false

    /**
     * This function rotates the player cards clock wise
     */
    override fun refreshAfterPlayerSwitch() {
        if (nextPlayerSwitchDelayed) {
            lock()
            playAnimation(
                DelayAnimation(duration = 2000).apply {
                    onFinished =
                        {
                            actualPlayerSwitch()
                            unlock()
                        }
                }
            )
        } else {
            actualPlayerSwitch()
        }
    }

    private fun actualPlayerSwitch() {
        val currentGame = requireNotNull(rootService.currentGame)
        when (currentGame.players.size) {
            2 -> {
                val cardsBottom = cardPaneBottom.components
                val cardsTop = cardPaneTop.components

                topNameLabel.text = currentGame.players[(currentGame.currentPlayer + 1) % 2].name
                cardPaneTop.removeAll(cardsTop)
                middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
                cardPaneBottom.removeAll(cardsBottom)

                cardPaneTop.addAll(cardsBottom)
                cardPaneBottom.addAll(cardsTop)

                cardPaneTop.components.onEach {
                    it.showBack()
                }
            }
            3 -> {
                val cardsBottom = cardPaneBottom.components
                val cardsLeft = cardPaneLeft.components
                val cardsRight = cardPaneRight.components
                leftNameLabel.text = currentGame.players[(currentGame.currentPlayer + 2) % 3].name
                cardPaneLeft.removeAll(cardsLeft)
                middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
                cardPaneBottom.removeAll(cardsBottom)
                rightNameLabel.text = currentGame.players[(currentGame.currentPlayer + 1) % 3].name
                cardPaneRight.removeAll(cardsRight)

                cardPaneLeft.addAll(cardsBottom)
                cardPaneBottom.addAll(cardsRight)
                cardPaneRight.addAll(cardsLeft)

                cardPaneLeft.components.onEach {
                    it.showBack()
                }
                cardPaneRight.components.onEach {
                    it.showBack()
                }
            }
            4 -> {
                val cardsTop = cardPaneTop.components
                val cardsBottom = cardPaneBottom.components
                val cardsLeft = cardPaneLeft.components
                val cardsRight = cardPaneRight.components

                leftNameLabel.text = currentGame.players[(currentGame.currentPlayer + 3) % 4].name
                cardPaneLeft.removeAll(cardsLeft)
                middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
                cardPaneBottom.removeAll(cardsBottom)
                rightNameLabel.text = currentGame.players[(currentGame.currentPlayer + 1) % 4].name
                cardPaneRight.removeAll(cardsRight)
                topNameLabel.text = currentGame.players[(currentGame.currentPlayer + 2) % 4].name
                cardPaneTop.removeAll(cardsTop)

                cardPaneLeft.addAll(cardsBottom)
                cardPaneBottom.addAll(cardsRight)
                cardPaneRight.addAll(cardsTop)
                cardPaneTop.addAll(cardsLeft)

                cardPaneTop.components.onEach {
                    it.showBack()
                }
                cardPaneLeft.components.onEach {
                    it.showBack()
                }
                cardPaneRight.components.onEach {
                    it.showBack()
                }
            }
        }
        revealButton.isVisible = true
        score.text = "?"

        val passedCounter = currentGame.passedCounter
        for (i in 0 until currentGame.players.size) {
            if ((i + 1) <= passedCounter) {
                passRoundButtons[i]
                    .componentStyle = """
                        -fx-border-color: #a38c45;
                        -fx-border-width: 2px;
                        -fx-border-radius: 50%;
                        -fx-background-radius: 50%;
                        -fx-background-color: #f2de83;
                         """.trimIndent()
            } else {
                passRoundButtons[i]
                    .componentStyle = """
                        -fx-border-color: #666666;
                        -fx-border-width: 2px;
                        -fx-border-radius: 50%;
                        -fx-background-radius: 50%;
                        -fx-background-color: #F5F5F5;
                         """.trimIndent()
            }
        }
        nextPlayerSwitchDelayed = false
    }

    /**
     * This function switches one playerCard with one tableCard
     */
    override fun refreshAfterSwitchOne(playerCard: Card, tableCard: Card) {
        nextPlayerSwitchDelayed = true
        val currentGame = requireNotNull(rootService.currentGame)
        val playerCardIndex = currentGame.players[currentGame.currentPlayer].cardsOnHand.indexOf(tableCard)
        val tableCardIndex = currentGame.cardsInMid.indexOf(playerCard)
        val newBottomComponents = cardPaneBottom.components.toMutableList()
        val newMiddleComponents = cardPaneMiddle.components.toMutableList()
        val newBottomCard = newMiddleComponents[tableCardIndex]
        val newTableCard = newBottomComponents[playerCardIndex]

        newBottomCard.rotation = -15.0 + (playerCardIndex * 15.0)
        newBottomCard.posY = 20.0 - (20 * (playerCardIndex % 2))
        newBottomCard.posX = (cardWidth + 30.0) * playerCardIndex

        newTableCard.rotation = 0.0
        newTableCard.posY = 0.0
        newTableCard.posX = (cardWidth + 30.0) * tableCardIndex

        setCardIndex(newBottomCard, playerCardIndex, false)
        setCardIndex(newTableCard, tableCardIndex, true)

        newBottomComponents.removeAt(playerCardIndex)
        newBottomComponents.add(playerCardIndex, newBottomCard)
        newMiddleComponents.removeAt(tableCardIndex)
        newMiddleComponents.add(tableCardIndex, newTableCard)

        cardPaneBottom.removeAll(cardPaneBottom.components)
        cardPaneMiddle.removeAll(cardPaneMiddle.components)
        cardPaneBottom.addAll(newBottomComponents)
        cardPaneMiddle.addAll(newMiddleComponents)

        switchAllButton.isDisabled = false
        passButton.isDisabled = false
        closeButton.isDisabled = false

        chosenHandCard = null
        chosenTableCard = null

        switchOnePressed = false
    }

    /**
     * This function switches all player cards with all table cards
     */
    override fun refreshAfterSwitchAll() {
        nextPlayerSwitchDelayed = true
        val currentGame = requireNotNull(rootService.currentGame)
        val newCardsOnHand = createCardViews(
            currentGame.players[currentGame.currentPlayer].cardsOnHand, true
        )
        newCardsOnHand.onEach {
            it.showFront()
        }
        cardPaneBottom.clear()
        cardPaneBottom.addAll(newCardsOnHand)
    }

    /**
     * This function switches all table cards with new cards from the deck
     */
    override fun refreshAfterTableDeckChange() {
        val currentGame = requireNotNull(rootService.currentGame)
        val newCardsInMid = createCardViews(currentGame.cardsInMid, false)
        newCardsInMid.onEach {
            it.showFront()
        }
        cardPaneMiddle.clear()
        cardPaneMiddle.addAll(newCardsInMid)
    }

    /*/**
     * This function increases the pass visuals in the pass button
     */
    override fun refreshAfterPass() {

    }*/

    /**
     * This function changes the middleLabel after a player has closed the game
     */
    override fun refreshAfterClose() {
        middleLabel.text = "It`s your last turn"
    }
}