package service

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * This is a class for testing the [RootService] functions
 */
class TestRootService {
    /**
     * Function for testing the creation of a [RootService] instance
     */
    @Test
    fun testCreateRootService() {
        val testRootService = RootService()
        assertNotNull(testRootService)
        assertNotNull(testRootService.gameService)
        assertNotNull(testRootService.playerActionService)
        assertNull(testRootService.currentGame)
    }
}