package view

import service.RootService
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * This is a class to represent the scroeboard at the end of a game
 */
class ScoreboardScene(private val rootService: RootService) : MenuScene(960, 720), Refreshable {
    private val pane: Pane<UIComponent> = Pane(
        width = this.width - 160, height = this.height - 280,
        posX = (this.width / 2) - ((this.width - 160) / 2), posY = 80
    )

    private val headlineLabelWidth = 400
    private val headlineLabel: UIComponent = Label(
        width = headlineLabelWidth, height = 50, posX = (pane.width / 2) - (headlineLabelWidth / 2), posY = 25,
        text = "Score Board",
        font = Font(size = 50)
    )

    private val playerName1: Label = Label(
        width = 400, height = 50, posX = 50, posY = headlineLabel.posY + headlineLabel.height + 50,
        text = "#1 Player1",
        font = Font(size = 40),
        alignment = Alignment.CENTER_LEFT
    )

    private val playerScore1: Label = Label(
        width = 150, height = 50, posX = 650, posY = headlineLabel.posY + headlineLabel.height + 50,
        text = "31",
        font = Font(size = 40)
    )

    private val playerName2: Label = Label(
        width = 400, height = 50, posX = 50, posY = playerName1.posY + playerName1.height + 25,
        text = "#2 Player2",
        font = Font(size = 40),
        alignment = Alignment.CENTER_LEFT
    )

    private val playerScore2: Label = Label(
        width = 150, height = 50, posX = 650, posY = playerName1.posY + playerName1.height + 25,
        text = "30.5",
        font = Font(size = 40)
    )

    private val playerName3: Label = Label(
        width = 400, height = 50, posX = 50, posY = playerName2.posY + playerName2.height + 25,
        text = "#3 Player3",
        font = Font(size = 40),
        alignment = Alignment.CENTER_LEFT
    )

    private val playerScore3: Label = Label(
        width = 150, height = 50, posX = 650, posY = playerName2.posY + playerName2.height + 25,
        text = "28",
        font = Font(size = 40)
    )

    private val playerName4: Label = Label(
        width = 400, height = 50, posX = 50, posY = playerName3.posY + playerName3.height + 25,
        text = "#4 Player4",
        font = Font(size = 40),
        alignment = Alignment.CENTER_LEFT
    )

    private val playerScore4: Label = Label(
        width = 150, height = 50, posX = 650, posY = playerName3.posY + playerName3.height + 25,
        text = "25",
        font = Font(size = 40)
    )

    private val buttonWidth = 300
    private val buttonFontSize = 30

    private val playAgainButton = Button(
        width = buttonWidth, height = 100, posX = 0, posY = 0,
        text = "Play Again",
        font = Font(size = buttonFontSize),
        visual = Visual.EMPTY
    ).apply {
        componentStyle = """
            -fx-border-color: #8a9e5d;
            -fx-background-color: #cdeb8b;
            -fx-border-width: 1px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
                """.trimIndent()
        onMouseClicked = {
            val currentGame = requireNotNull(rootService.currentGame)
            val playerNames = arrayOfNulls<String>(currentGame.players.size)
            for (i in playerNames.indices) {
                playerNames[i] = currentGame.players[i].name
            }
            rootService.gameService.startNewGame(currentGame.players.size, playerNames.requireNoNulls())
        }
    }

    /**
     * This menu button brings a player back to the [NewGameScene]
     */
    val menuButton = Button(
        width = buttonWidth, height = 100, posX = playAgainButton.posX + playAgainButton.width + 50, posY = 0,
        text = "Menu",
        font = Font(size = buttonFontSize),
        visual = Visual.EMPTY
    ).apply {
        componentStyle = """
            -fx-border-color: #8ea0b2;
            -fx-background-color: #cce5ff;
            -fx-border-width: 1px;
            -fx-border-radius: 20px;
            -fx-background-radius: 20px;
                """.trimIndent()
    }

    private val buttonPane: Pane<Button> = Pane(
        width = 650, height = 100,
        posX = (this.width / 2) - 325, posY = pane.posY + pane.height + 50,
    )

    private val playerNames = arrayOf(playerName1, playerName2, playerName3, playerName4)
    private val playerScores = arrayOf(playerScore1, playerScore2, playerScore3, playerScore4)

    init {
        background = ColorVisual(238, 238, 238)
        playerNames.onEach {
            it.isVisible = false
        }
        playerScores.onEach {
            it.isVisible = false
        }
        pane.apply {
            visual = ColorVisual(255, 242, 204)
            addAll(
                headlineLabel,
                playerName1,
                playerScore1,
                playerName2,
                playerScore2,
                playerName3,
                playerScore3,
                playerName4,
                playerScore4
            )
        }
        buttonPane.apply {
            addAll(
                playAgainButton,
                menuButton
            )
        }
        addComponents(
            pane,
            buttonPane
        )
    }

    /**
     * This function sets the labels on the scoreboard
     */
    override fun refreshAfterGameEnd() {
        val currentGame = requireNotNull(rootService.currentGame)
        val players = currentGame.players.toMutableList()
        players.sortBy {
            rootService.gameService.countPoints(it)
        }
        players.reverse()
        for (i in 0 until players.size) {
            playerNames[i].text = "#" + (i + 1).toString() + " " + players[i].name
            playerScores[i].text = rootService.gameService.countPoints(players[i]).toString()
            playerNames[i].isVisible = true
            playerScores[i].isVisible = true
        }
    }
}