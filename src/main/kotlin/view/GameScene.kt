package view

import entity.Card
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class GameScene(private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {

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
                    } else if (chosenTableCard != null && !isMiddleCard) {
                        rootService.playerActionService.switchOne(
                            currentGame.players[currentGame.currentPlayer],
                            currentGame.players[currentGame.currentPlayer].cardsOnHand[index],
                            requireNotNull(chosenTableCard)
                        )
                    } else {
                        if (isMiddleCard) {
                            chosenTableCard = currentGame.cardsInMid[index]
                        } else {
                            chosenHandCard = currentGame.players[currentGame.currentPlayer].cardsOnHand[index]
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
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = 150,
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
        posX = cardPaneBottom.posX + (cardPaneBottom.width / 2) - 150,
        posY = cardPaneBottom.posY + (cardPaneBottom.height / 2) - 20,
        height = 75, width = 300,
        text = "Reveal Cards",
        font = Font(size = 40),
        visual = ColorVisual(248, 206, 204)
    ).apply {
        onMouseClicked = {
            revealCards()
        }
    }

    private val middleNameLabel = Label(
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = (this.height / 2) + 25,
        text = "",
        font = Font(size = 40)
    )

    private val middleLabel = Label(
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = middleNameLabel.posY + 50,
        text = "It`s your turn",
        font = Font(size = 20, fontStyle = Font.FontStyle.ITALIC)
    )

    private val actionButtonWidth = 300
    private val actionButtonFontSize = 30

    private var switchOnePressed = false

    private val switchOneButton = Button(
        width = actionButtonWidth, height = 100, posX = 0, posY = 0,
        text = "Switch One Card",
        font = Font(size = actionButtonFontSize),
        visual = ColorVisual(255, 242, 204)
    ).apply {
        onMouseClicked = {
            switchOnePressed = true
        }
    }

    private val switchAllButton = Button(
        width = actionButtonWidth, height = 100, posX = switchOneButton.posX + switchOneButton.width + 50, posY = 0,
        text = "Switch All Cards",
        font = Font(size = actionButtonFontSize),
        visual = ColorVisual(255, 230, 204)
    ).apply {
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            rootService.playerActionService.switchAll(currentGame.players[currentGame.currentPlayer])
        }
    }

    private val passButton = Button(
        width = actionButtonWidth, height = 100, posX = switchAllButton.posX + switchAllButton.width + 50, posY = 0,
        text = "Pass",
        font = Font(size = actionButtonFontSize),
        visual = ColorVisual(245, 245, 245)
    ).apply {
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            rootService.playerActionService.pass(currentGame.players[currentGame.currentPlayer])
        }
    }

    private val closeButton = Button(
        width = actionButtonWidth, height = 100, posX = passButton.posX + passButton.width + 50, posY = 0,
        text = "Close",
        font = Font(size = actionButtonFontSize),
        visual = ColorVisual(248, 206, 204)
    ).apply {
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            rootService.playerActionService.close(currentGame.players[currentGame.currentPlayer])
        }
    }

    private val buttonPane: Pane<Button> = Pane<Button>(
        width = 1250, height = 100,
        posX = (this.width / 2) - 675, posY = cardPaneBottom.posY + 275,
    ).apply {
        addAll(listOf(switchOneButton, switchAllButton, passButton, closeButton))
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
        background = ColorVisual(213, 232, 212, 255)
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

    fun initCardPanes() {
        val currentGame = requireNotNull(rootService.currentGame)
        when (currentGame.players.size) {
            2 -> {
                cardPaneLeft.isVisible = false
                cardPaneRight.isVisible = false
                leftNameLabel.isVisible = false
                rightNameLabel.isVisible = false
                topNameLabel.text = currentGame.players[currentGame.currentPlayer + 1].name
                cardPaneTop.apply {
                    addAll(createCardViews(currentGame.players[1].cardsOnHand, true))
                }
            }
            3 -> {
                cardPaneTop.isVisible = false
                topNameLabel.isVisible = false
                leftNameLabel.text = currentGame.players[2].name
                cardPaneLeft.apply {
                    addAll(createCardViews(currentGame.players[2].cardsOnHand, true))
                }
                rightNameLabel.text = currentGame.players[1].name
                cardPaneRight.apply {
                    addAll(createCardViews(currentGame.players[1].cardsOnHand, true))
                }
            }
            4 -> {
                leftNameLabel.text = currentGame.players[3].name
                cardPaneLeft.apply {
                    addAll(createCardViews(currentGame.players[3].cardsOnHand, true))
                }
                rightNameLabel.text = currentGame.players[1].name
                cardPaneRight.apply {
                    addAll(createCardViews(currentGame.players[1].cardsOnHand, true))
                }
                topNameLabel.text = currentGame.players[2].name
                cardPaneTop.apply {
                    addAll(createCardViews(currentGame.players[2].cardsOnHand, true))
                }
            }
        }

        cardPaneBottom.apply {
            addAll(createCardViews(currentGame.players[0].cardsOnHand, true))
        }

        val cardsInMid = currentGame.cardsInMid
        cardPaneMiddle.addAll(createCardViews(cardsInMid, false))
        cardPaneMiddle.onEach {
            it.showFront()
        }

        middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
    }

    private fun revealCards() {
        cardPaneBottom.onEach {
            it.showFront()
        }
        revealButton.apply {
            isVisible = false
        }
        score.apply {
            val currentGame = requireNotNull(rootService.currentGame)
            text = rootService.gameService.countPoints(currentGame.players[currentGame.currentPlayer]).toString()
        }
    }

    override fun refreshAfterPlayerSwitch() {
        val currentGame = requireNotNull(rootService.currentGame)
        when (currentGame.players.size) {
            2 -> {
                val cardsBottom = cardPaneBottom.components
                val cardsTop = cardPaneTop.components
                topNameLabel.text = currentGame.players[(currentGame.currentPlayer + 1) % 2].name
                cardPaneTop.apply {
                    removeAll(cardsTop)
                }
                middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
                cardPaneBottom.apply {
                    removeAll(cardsBottom)
                }

                cardPaneTop.apply {
                    addAll(cardsBottom)
                }
                cardPaneBottom.apply {
                    addAll(cardsTop)
                }

                cardPaneTop.components.onEach {
                    it.showBack()
                }
            }
            3 -> {
                val cardsBottom = cardPaneBottom.components
                val cardsLeft = cardPaneLeft.components
                val cardsRight = cardPaneRight.components
                leftNameLabel.text = currentGame.players[(currentGame.currentPlayer + 2) % 3].name
                cardPaneLeft.apply {
                    removeAll(cardsLeft)
                }
                middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
                cardPaneBottom.apply {
                    removeAll(cardsBottom)
                }
                rightNameLabel.text = currentGame.players[(currentGame.currentPlayer + 1) % 3].name
                cardPaneRight.apply {
                    removeAll(cardsRight)
                }

                cardPaneLeft.apply {
                    addAll(cardsBottom)
                }
                cardPaneBottom.apply {
                    addAll(cardsRight)
                }
                cardPaneRight.apply {
                    addAll(cardsLeft)
                }

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
                cardPaneLeft.apply {
                    removeAll(cardsLeft)
                }
                middleNameLabel.text = currentGame.players[currentGame.currentPlayer].name
                cardPaneBottom.apply {
                    removeAll(cardsBottom)
                }
                rightNameLabel.text = currentGame.players[(currentGame.currentPlayer + 1) % 4].name
                cardPaneRight.apply {
                    removeAll(cardsRight)
                }
                topNameLabel.text = currentGame.players[(currentGame.currentPlayer + 2) % 4].name
                cardPaneTop.apply {
                    removeAll(cardsTop)
                }

                cardPaneLeft.apply {
                    addAll(cardsBottom)
                }
                cardPaneBottom.apply {
                    addAll(cardsRight)
                }
                cardPaneRight.apply {
                    addAll(cardsTop)
                }
                cardPaneTop.apply {
                    addAll(cardsLeft)
                }

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
        score.apply {
            text = "?"
        }
    }

    override fun refreshAfterSwitchOne(playerCard: Card, tableCard: Card) {
        val currentGame = requireNotNull(rootService.currentGame)
        val playerCardIndex = currentGame.players[currentGame.currentPlayer].cardsOnHand.indexOf(tableCard)
        val tableCardIndex = currentGame.cardsInMid.indexOf(playerCard)
        val newBottomComponents = cardPaneBottom.components.toMutableList()
        val newMiddleComponents = cardPaneMiddle.components.toMutableList()
        val newBottomCard = newMiddleComponents[tableCardIndex]
        val newTableCard = newBottomComponents[playerCardIndex]

        newBottomCard.apply {
            rotation = -15.0 + (playerCardIndex * 15.0)
            posY = 20.0 - (20 * (playerCardIndex % 2))
            posX = (cardWidth + 30.0) * playerCardIndex
        }
        newTableCard.apply {
            rotation = 0.0
            posY = 0.0
            posX = (cardWidth + 30.0) * tableCardIndex
        }

        setCardIndex(newBottomCard, playerCardIndex, false)
        setCardIndex(newTableCard, tableCardIndex, true)

        newBottomComponents.removeAt(playerCardIndex)
        newBottomComponents.add(playerCardIndex, newBottomCard)
        newMiddleComponents.removeAt(tableCardIndex)
        newMiddleComponents.add(tableCardIndex, newTableCard)
        cardPaneBottom.apply {
            removeAll(cardPaneBottom.components)
        }
        cardPaneMiddle.apply {
            removeAll(cardPaneMiddle.components)
        }
        cardPaneBottom.apply {
            addAll(newBottomComponents)
        }
        cardPaneMiddle.apply {
            addAll(newMiddleComponents)
        }

        chosenHandCard = null
        chosenTableCard = null

        switchOnePressed = false
    }

    override fun refreshAfterSwitchAll() {
        val currentGame = requireNotNull(rootService.currentGame)
        val newCardsOnHand = createCardViews(
            currentGame.players[currentGame.currentPlayer].cardsOnHand, true
        )
        cardPaneBottom.apply {
            removeAll(cardPaneMiddle.components)
            addAll(newCardsOnHand)
        }
    }

    override fun refreshAfterTableDeckChange() {
        val currentGame = requireNotNull(rootService.currentGame)
        val newCardsInMid = createCardViews(currentGame.cardsInMid, false)
        newCardsInMid.onEach {
            it.showFront()
        }
        cardPaneMiddle.apply {
            removeAll(cardPaneMiddle.components)
            addAll(newCardsInMid)
        }
    }

    override fun refreshAfterPass() {
        //TODO: implement a visual pass counter and update
    }

    override fun refreshAfterClose() {
        middleLabel.text = "It`s your last turn"
    }
}