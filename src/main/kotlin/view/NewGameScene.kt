package view

import service.RootService
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

class NewGameScene(private val rootService: RootService) : MenuScene(1920, 1080), Refreshable {

    private var playerCount = 2

    private val pane: Pane<UIComponent> = Pane(
        width = this.width - 80, height = this.height - 300, posX = 40, posY = 40
    )

    private val headlineLabelWidth = 400
    private val headlineLabel: UIComponent = Label(
        width = headlineLabelWidth, height = 50, posX = (pane.width / 2) - (headlineLabelWidth / 2), posY = 50,
        text = "Swim",
        font = Font(size = 60)
    )


    private val numberOfPlayersLabel = Label(
        width = 400, height = 50, posX = 200, posY = 200,
        text = "Number of players:",
        font = Font(size = 40)
    )

    private val numberOfPlayersBox = ComboBox(
        posX = numberOfPlayersLabel.posX + numberOfPlayersLabel.width, posY = 200, width = 100, height = 50,
        font = Font(size = 20),
        items = mutableListOf(2, 3, 4)
    ).apply {
        selectedItemProperty.addListener { _, newValue ->
            if (newValue != null) {
                playerCount = newValue
            }
            when (newValue) {
                2 -> {
                    p3Input.isVisible = false
                    p3Label.isVisible = false
                    p4Input.isVisible = false
                    p4Label.isVisible = false
                }
                3 -> {
                    p3Input.isVisible = true
                    p3Label.isVisible = true
                    p4Input.isVisible = false
                    p4Label.isVisible = false
                }
                4 -> {
                    p3Input.isVisible = true
                    p3Label.isVisible = true
                    p4Input.isVisible = true
                    p4Label.isVisible = true
                }
            }
        }
    }

    private val p1Label = Label(
        posX = 1000, posY = 200, width = 250, height = 50,
        text = "Player 1:",
        font = Font(size = 40)
    )

    private val p1Input: TextField = TextField(
        posX = p1Label.posX + p1Label.width, posY = 200, width = 400, height = 50,
        text = "",
        font = Font(size = 40),
        prompt = "Mike"
    ).apply {
        onKeyTyped = {
            newGameButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank()
        }
    }

    private val p2Label = Label(
        posX = 1000, posY = p1Label.posY + 125, width = 250, height = 50,
        text = "Player 2:",
        font = Font(size = 40)
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p2Input: TextField = TextField(
        posX = p2Label.posX + p2Label.width, posY = p1Input.posY + 125, width = 400, height = 50,
        text = "",
        font = Font(size = 40),
        prompt = "Dustin"
    ).apply {
        onKeyTyped = {
            newGameButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val p3Label = Label(
        posX = 1000, posY = p2Label.posY + 125, width = 250, height = 50,
        text = "Player 3:",
        font = Font(size = 40)
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p3Input: TextField = TextField(
        posX = p3Label.posX + p3Label.width, posY = p2Input.posY + 125, width = 400, height = 50,
        text = "",
        font = Font(size = 40),
        prompt = "Lucas"
    ).apply {
        onKeyTyped = {
            newGameButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val p4Label = Label(
        posX = 1000, posY = p3Label.posY + 125, width = 250, height = 50,
        text = "Player 4:",
        font = Font(size = 40)
    )

    // type inference fails here, so explicit  ": TextField" is required
    // see https://discuss.kotlinlang.org/t/unexpected-type-checking-recursive-problem/6203/14
    private val p4Input: TextField = TextField(
        posX = p4Label.posX + p4Label.width, posY = p3Input.posY + 125, width = 400, height = 50,
        text = "",
        font = Font(size = 40),
        prompt = "Will"
    ).apply {
        onKeyTyped = {
            newGameButton.isDisabled = p1Input.text.isBlank() || this.text.isBlank()
        }
    }

    private val errorLabel = Label(
        width = 450, height = 50, posX = (this.width / 2) - 225, posY = pane.posY + pane.height,
        text = "Player names must be unique",
        font = Font(size = 20, fontStyle = Font.FontStyle.ITALIC, color = Color.RED)
    ).apply {
        isVisible = false
    }

    private val buttonWidth = 300
    private val newGameButton: Button = Button(
        height = 120,
        width = buttonWidth,
        posX = (this.width / 2) - (buttonWidth / 2),
        posY = pane.posY + pane.height + 70,
        text = "Start Game",
        font = Font(color = Color.BLACK, size = 40),
    ).apply {
        visual = ColorVisual(205, 235, 139, 255)
        onMouseClicked = {
            var players = arrayOfNulls<String>(playerCount)
            when (playerCount) {
                2 -> {
                    players = arrayOf(
                        p1Input.text.trim(),
                        p2Input.text.trim()
                    )
                }
                3 -> {
                    players = arrayOf(
                        p1Input.text.trim(),
                        p2Input.text.trim(),
                        p3Input.text.trim()
                    )
                }
                4 -> {
                    players = arrayOf(
                        p1Input.text.trim(),
                        p2Input.text.trim(),
                        p3Input.text.trim(),
                        p4Input.text.trim()
                    )
                }
            }
            val standardNames = arrayOf("Mike", "Dustin", "Lucas", "Will")
            for (i in players.indices) {
                if (players[i] == "") {
                    players[i] = standardNames[i]
                }
            }
            if (players.distinct().toTypedArray().contentEquals(players)) {
                rootService.gameService.startNewGame(
                    playerCount,
                    players.requireNoNulls()
                )
            } else {
                errorLabel.isVisible = true
            }
        }
    }

    init {
        opacity = 1.0
        background = ColorVisual(213, 232, 212)
        p3Label.isVisible = false
        p3Input.isVisible = false
        p4Label.isVisible = false
        p4Input.isVisible = false
        pane.apply {
            addAll(
                headlineLabel,
                numberOfPlayersLabel,
                numberOfPlayersBox,
                p1Label,
                p1Input,
                p2Label,
                p2Input,
                p3Label,
                p3Input,
                p4Label,
                p4Input,
            )
            visual = ColorVisual(255, 242, 204, 255)
        }
        addComponents(
            errorLabel,
            newGameButton,
            pane
        )
    }

}