package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Implementation of the BGW [BoardGameApplication] for the card game "Swim"
 */
class SopraApplication : BoardGameApplication("SoPra Game"), Refreshable {

    private val rootService = RootService()

    private val newGameScene = NewGameScene(rootService)
    private val gameScene = GameScene(rootService)
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

    }

    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
        gameScene.initCardPanes()
    }

    override fun refreshAfterGameEnd() {
        this.showMenuScene(scoreboardScene)
    }

}

