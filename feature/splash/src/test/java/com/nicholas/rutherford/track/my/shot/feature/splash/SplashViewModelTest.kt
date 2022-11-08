package com.nicholas.rutherford.track.my.shot.feature.splash

import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.NavigatorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SplashViewModelTest {

    lateinit var viewModel: SplashViewModel

    internal var navigator = NavigatorImpl()

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(navigator = navigator)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Nested
    inner class Constants {

        @Test
        fun `splash delay in millis`() {
            Assertions.assertEquals(SPLASH_DELAY_IN_MILLIS, 4000L)
        }

        @Test
        fun `splash image scale`() {
            Assertions.assertEquals(SPLASH_IMAGE_SCALE, 1f)
        }
    }

    @Nested
    inner class Init {

        @Test fun initalizeSplashState() {
            Assertions.assertEquals(
                viewModel.splashState.value,
                viewModel.initializeSplashState
            )
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `should delay for 4 seconds and navigate to home`() = runTest {
            delay(4000)

            Assertions.assertEquals(viewModel.navigationDestination, NavigationActions.SplashScreen.navigateToHome())
        }
    }
}
