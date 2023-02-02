package com.nicholas.rutherford.track.my.shot.feature.splash

import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.shared.preferences.read.ReadSharedPreferences
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
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

    internal val readSharedPreferences = mockk<ReadSharedPreferences>()

    internal var navigation = mockk<SplashNavigation>(relaxed = true)
    internal var readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)

    internal val delayTime = 4001L // needs 1 extra millisecond to account for function below call

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
        viewModel = SplashViewModel(readSharedPreferences = readSharedPreferences, navigation = navigation, readFirebaseUserInfo = readFirebaseUserInfo)
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

        @Test
        fun initalizeSplashState() {
            Assertions.assertEquals(
                viewModel.splashStateFlow.value,
                SplashState(
                    backgroundColor = Colors.primaryColor,
                    imageScale = SPLASH_IMAGE_SCALE,
                    imageDrawableId = DrawablesIds.splash
                )
            )
        }

        @Nested
        inner class NavigateToAuthentication {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to true, isEmailVerified set to false, and accountHasBeenCreated set to false should navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedIn } returns true
                    every { readFirebaseUserInfo.isEmailVerified } returns false
                    every { readSharedPreferences.accountHasBeenCreated() } returns false

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        readSharedPreferences = readSharedPreferences,
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo
                    )

                    delay(delayTime)

                    coVerify { navigation.navigateToAuthentication() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to false, isEmailVerified is set ot false, and accountHasBeenCreated set to false should not navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedIn } returns false
                    every { readFirebaseUserInfo.isEmailVerified } returns false
                    every { readSharedPreferences.accountHasBeenCreated() } returns false

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        readSharedPreferences = readSharedPreferences,
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo
                    )

                    delay(delayTime)

                    coVerify(exactly = 0) { navigation.navigateToAuthentication() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to true, isEmailVerified is set true, and accountHasBeenCreated set to false should not navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedIn } returns true
                    every { readFirebaseUserInfo.isEmailVerified } returns true
                    every { readSharedPreferences.accountHasBeenCreated() } returns false

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        readSharedPreferences = readSharedPreferences,
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo
                    )

                    delay(delayTime)

                    coVerify(exactly = 0) { navigation.navigateToAuthentication() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `when isLoggedIn is set to true, isEmailVerified is set false, and accountHasBeenCreated set to true should not navigateToAuthentication`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedIn } returns true
                    every { readFirebaseUserInfo.isEmailVerified } returns false
                    every { readSharedPreferences.accountHasBeenCreated() } returns true

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        readSharedPreferences = readSharedPreferences,
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo
                    )

                    delay(delayTime)

                    coVerify(exactly = 0) { navigation.navigateToAuthentication() }
                }
        }

        @Nested
        inner class NavigateToHomeOrLogin {

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `should delay for 4 seconds and verifies that it calls navigateToLogin when isLoggedIn is set to false`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedIn } returns false
                    every { readFirebaseUserInfo.isEmailVerified } returns true
                    every { readSharedPreferences.accountHasBeenCreated() } returns false

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        readSharedPreferences = readSharedPreferences,
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo
                    )

                    delay(delayTime)

                    coVerify { navigation.navigateToLogin() }
                }

            @OptIn(ExperimentalCoroutinesApi::class)
            @Test
            fun `should delay for 4 seconds and verifies it calls navigateToHome when isLoggedIn is set to true`() =
                runTest {
                    every { readFirebaseUserInfo.isLoggedIn } returns true
                    every { readFirebaseUserInfo.isEmailVerified } returns true
                    every { readSharedPreferences.accountHasBeenCreated() } returns false

                    Dispatchers.setMain(dispatcher)
                    viewModel = SplashViewModel(
                        readSharedPreferences = readSharedPreferences,
                        navigation = navigation,
                        readFirebaseUserInfo = readFirebaseUserInfo
                    )

                    delay(delayTime)

                    coVerify { navigation.navigateToHome() }
                }
        }
    }
}
