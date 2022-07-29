package view

import tools.aqua.bgw.core.BoardGameApplication

/**
 * Implementation of the BGW [BoardGameApplication] for the card game "Swim"
 */
class SopraApplication : BoardGameApplication("SoPra Game") {

    /**
     * Example hello scene
     */
    private val helloScene = HelloScene()

    init {
        this.showGameScene(helloScene)
    }

}

