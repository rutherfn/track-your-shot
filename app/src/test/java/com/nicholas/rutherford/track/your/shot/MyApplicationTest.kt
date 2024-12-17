package com.nicholas.rutherford.track.your.shot

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordNavigation
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
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

    private val createFirebaseUserInfo: CreateFirebaseUserInfo by inject()
    private val authenticationFirebase: AuthenticationFirebase by inject()
    private val readFirebaseUserInfo: ReadFirebaseUserInfo by inject()
    private val existingUserFirebase: ExistingUserFirebase by inject()

    private val network: Network by inject()
    private val buildType: BuildType by inject()
    private val appCenter: AppCenter by inject()
    private val navigator: Navigator by inject()

    private val splashNavigation: SplashNavigation by inject()
    private val loginNavigation: LoginNavigation by inject()
    private val forgotPasswordNavigation: ForgotPasswordNavigation by inject()
    private val createAccountNavigation: CreateAccountNavigation by inject()
    private val authenticationNavigation: AuthenticationNavigation by inject()

    private val mainActivityViewModel: MainActivityViewModel by inject()
    private val loginViewModel: LoginViewModel by inject()
    private val forgotPasswordViewModel: ForgotPasswordViewModel by inject()

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
        mockkStatic(FirebaseStorage::class)

        every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        every { FirebaseStorage.getInstance() } returns mockk(relaxed = true)

        myApplication.startKoinOnCreate()

        assertNotNull(createFirebaseUserInfo)
        assertNotNull(authenticationFirebase)
        assertNotNull(readFirebaseUserInfo)
        assertNotNull(existingUserFirebase)

        assertNotNull(network)
        assertNotNull(buildType)
        assertNotNull(appCenter)
        assertNotNull(navigator)

        assertNotNull(splashNavigation)
        assertNotNull(loginNavigation)
        assertNotNull(forgotPasswordNavigation)
        assertNotNull(createAccountNavigation)
        assertNotNull(authenticationNavigation)

        // todo figure out a way to mock shared preferences in classes that it gets used
        // the tests for some reason is not able to create the factory generated class

        // assertNotNull(createAccountViewModel)
        // assertNotNull(authenticationViewModel)
        // assertNotNull(splashViewModel)
        // assertNotNull(mainActivityViewModel)
        // assertNotNull(loginViewModel)
        // assertNotNull(forgotPasswordViewModel)
    }
}
