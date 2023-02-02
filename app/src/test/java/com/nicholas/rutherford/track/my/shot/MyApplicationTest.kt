package com.nicholas.rutherford.track.my.shot

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.app.center.AppCenter
import com.nicholas.rutherford.track.my.shot.build.type.BuildType
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordNavigation
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel
import com.nicholas.rutherford.track.my.shot.firebase.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.my.shot.firebase.util.AuthenticationFirebase
import com.nicholas.rutherford.track.my.shot.helper.constants.SharedPreferencesConstants
import com.nicholas.rutherford.track.my.shot.helper.network.Network
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import com.nicholas.rutherford.track.my.shot.shared.preferences.create.CreateSharedPreferences
import com.nicholas.rutherford.track.my.shot.shared.preferences.read.ReadSharedPreferences
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

    private val readSharedPreferences: ReadSharedPreferences by inject()

    private val firebaseAuth: FirebaseAuth by inject()
    private val firebaseDatabase: FirebaseDatabase by inject()

    private val createFirebaseUserInfo: CreateFirebaseUserInfo by inject()
    private val authenticationFirebase: AuthenticationFirebase by inject()
    private val readFirebaseUserInfo: ReadFirebaseUserInfo by inject()

    private val network: Network by inject()
    private val buildType: BuildType by inject()
    private val appCenter: AppCenter by inject()
    private val navigator: Navigator by inject()

    private val createSharedPreferences: CreateSharedPreferences by inject()

    private val splashNavigation: SplashNavigation by inject()
    private val loginNavigation: LoginNavigation by inject()
    private val forgotPasswordNavigation: ForgotPasswordNavigation by inject()
    private val createAccountNavigation: CreateAccountNavigation by inject()
    private val authenticationNavigation: AuthenticationNavigation by inject()

    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val splashViewModel: SplashViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()
    private val homeViewModel: HomeViewModel by inject()
    private val forgotPasswordViewModel: ForgotPasswordViewModel by inject()
    private val createAccountViewModel: CreateAccountViewModel by inject()
    private val authenticationViewModel: AuthenticationViewModel by inject()

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
        mockkStatic(FirebaseDatabase::class)

        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)

        myApplication.startKoinOnCreate()

        assertNotNull(readSharedPreferences)

        assertNotNull(firebaseAuth)
        assertNotNull(firebaseDatabase)

        assertNotNull(createFirebaseUserInfo)
        assertNotNull(authenticationFirebase)
        assertNotNull(readFirebaseUserInfo)

        assertNotNull(network)
        assertNotNull(buildType)
        assertNotNull(appCenter)
        assertNotNull(navigator)

//        assertNotNull(createSharedPreferences)

        assertNotNull(splashNavigation)
        assertNotNull(loginNavigation)
        assertNotNull(forgotPasswordNavigation)
        assertNotNull(createAccountNavigation)
        assertNotNull(authenticationNavigation)

        assertNotNull(mainActivityViewModel)
        assertNotNull(splashViewModel)
        assertNotNull(loginViewModel)
        assertNotNull(homeViewModel)
        assertNotNull(forgotPasswordViewModel)
        assertNotNull(createAccountViewModel)
        assertNotNull(authenticationViewModel)
    }
}
