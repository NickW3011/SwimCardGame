package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Implementation of the BGW [BoardGameApplication] for the card game "Swim"
 */
class SopraApplication : BoardGameApplication("SoPra Game"), Refreshable {

    private val rootService = RootService()

    private var newGameScene = NewGameScene(rootService)
    private var gameScene = GameScene(rootService)
    private val scoreboardScene = ScoreboardScene(rootService)

    init {
        rootService.addRefreshables(
            this,
            newGameScene,
            gameScene,
            scoreboardScene
        )

        this.showGameScene(gameScene)
        this.showMenuScene(newGameScene, 100)

        scoreboardScene.menuButton.apply {
            onMouseClicked = {
                gameScene = GameScene(rootService)
                rootService.addRefreshables(
                    newGameScene,
                    gameScene
                )
                showMenuScene(newGameScene)
            }
        }

    }

    /**
     * After a new game is started the current menu scene is hidden and the game scene is shown
     */
    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
        showGameScene(gameScene)
        gameScene.initCardPanes()
    }

    /**
     * After the game is over the scoreboard is shown
     */
    override fun refreshAfterGameEnd() {
        this.showMenuScene(scoreboardScene)
    }

}

