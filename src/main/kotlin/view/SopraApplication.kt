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

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
        showGameScene(gameScene)
        gameScene.initCardPanes()
    }

    override fun refreshAfterGameEnd() {
        this.showMenuScene(scoreboardScene)
    }

}

