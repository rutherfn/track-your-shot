package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.assertNotNull

class MyApplicationTest : KoinTest {

    private val buildType: BuildType by inject()
    private val appCenter: AppCenter by inject()
    private val navigator: Navigator by inject()

    private val splashNavigation: SplashNavigation by inject()

    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val splashViewModel: SplashViewModel by inject()
    private val homeViewModel: HomeViewModel by inject()

    private val myApplication = MyApplication()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        androidContext(myApplication)
        modules(AppModule().modules)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `start koin on create should inject modules with instances as not null`() {
        myApplication.startKoinOnCreate()

        assertNotNull(buildType)
        assertNotNull(appCenter)
        assertNotNull(navigator)

        assertNotNull(splashNavigation)

        assertNotNull(mainActivityViewModel)
        assertNotNull(splashViewModel)
        assertNotNull(homeViewModel)
    }
}
