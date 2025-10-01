package com.nicholas.rutherford.track.your.shot.helper.coroutinetestrule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Created by Nicholas Rutherford, last edited on 2025-01-27
 *
 * A JUnit 5 extension that sets up and tears down the main coroutine dispatcher for testing.
 * This extension ensures that Dispatchers.Main is properly configured for unit tests,
 * preventing the "Dispatchers.Main was accessed when the platform dispatcher was absent" error.
 *
 * Usage:
 * ```kotlin
 * @ExtendWith(CoroutineTestRule::class)
 * class MyViewModelTest {
 *     // Your tests here
 * }
 * ```
 *
 * Or manually:
 * ```kotlin
 * class MyViewModelTest {
 *     private val coroutineTestRule = CoroutineTestRule()
 *
 *     @BeforeEach
 *     fun setup() {
 *         coroutineTestRule.beforeEach(null)
 *     }
 *
 *     @AfterEach
 *     fun cleanup() {
 *         coroutineTestRule.afterEach(null)
 *     }
 * }
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule : BeforeEachCallback, AfterEachCallback {

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    val testScope: CoroutineScope = CoroutineScope(SupervisorJob() + testDispatcher)

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }

    /**
     * Creates a test scope with the configured test dispatcher.
     * This is useful for ViewModels that need a CoroutineScope parameter.
     */
    fun createTestScope(): CoroutineScope = testScope
}
