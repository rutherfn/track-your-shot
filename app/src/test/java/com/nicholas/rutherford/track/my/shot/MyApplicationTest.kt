package com.nicholas.rutherford.track.my.shot

import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.helper.network.Network
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
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

    private val firebaseAuth: FirebaseAuth by inject()
    private val createFirebaseUserInfo: CreateFirebaseUserInfo by inject()

    private val network: Network by inject()
    private val buildType: BuildType by inject()
    private val appCenter: AppCenter by inject()
    private val navigator: Navigator by inject()

    private val splashNavigation: SplashNavigation by inject()
    private val loginNavigation: LoginNavigation by inject()

    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val splashViewModel: SplashViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()
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
        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)

        myApplication.startKoinOnCreate()

        assertNotNull(firebaseAuth)
        assertNotNull(createFirebaseUserInfo)

        assertNotNull(network)
        assertNotNull(buildType)
        assertNotNull(appCenter)
        assertNotNull(navigator)

        assertNotNull(splashNavigation)
        assertNotNull(loginNavigation)

        assertNotNull(mainActivityViewModel)
        assertNotNull(splashViewModel)
        assertNotNull(loginViewModel)
        assertNotNull(homeViewModel)
    }
}
