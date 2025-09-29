package com.nicholas.rutherford.track.your.shot.feature.settings

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SettingsViewModelTest {

    private lateinit var settingsViewModel: SettingsViewModel

    private var navigation = mockk<SettingsNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val sdkValue = 2
    private val debugVersionName = "debug"
    private val releaseVersionName = "release"
    private val stageVersionName = "stage"

    private val buildTypeDebug = BuildTypeImpl(sdkValue = sdkValue, buildTypeValue = debugVersionName)
    private val buildTypeRelease = BuildTypeImpl(sdkValue = sdkValue, buildTypeValue = releaseVersionName)
    private val buildTypeStage = BuildTypeImpl(sdkValue = sdkValue, buildTypeValue = stageVersionName)

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private val deleteFirebaseUserInfo = mockk<DeleteFirebaseUserInfo>(relaxed = true)
    private val accountManager = mockk<AccountManager>(relaxed = true)
    private val authenticationFirebase = mockk<AuthenticationFirebase>(relaxed = true)
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.manageDeclaredShots) } returns "Manage Declared Shots"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.settings) } returns "Settings"
        every { application.getString(StringsIds.accountInfo) } returns "Account Info"
        every { application.getString(StringsIds.termsConditions) } returns "Terms & Conditions"
        every { application.getString(StringsIds.usingTheApp) } returns "Using The App"
        every { application.getString(StringsIds.enabledPermissions) } returns "Enabled Permissions"
        every { application.getString(StringsIds.viewMoreInfo) } returns "View More Info"
        every { application.getString(StringsIds.settingsHelpDescription) } returns "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them."
        every { application.getString(StringsIds.debug) } returns "Debug"
        every { application.getString(StringsIds.deleteAccount) } returns "Delete Account"
        every { application.getString(StringsIds.inAppFirebaseViewer) } returns "In-App Firebase Viewer"
        every { application.getString(StringsIds.toggles) } returns "Toggles"
        every { application.getString(StringsIds.areYouSureYouWantToDeleteYourAccount) } returns "Are you sure you want to delete your account?"
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.no) } returns "No"
        every { application.getString(StringsIds.error) } returns "Error"

        settingsViewModel = SettingsViewModel(
            navigation = navigation,
            application = application,
            scope = scope,
            buildType = buildTypeDebug,
            activeUserRepository = activeUserRepository,
            deleteFirebaseUserInfo = deleteFirebaseUserInfo,
            accountManager = accountManager,
            authenticationFirebase = authenticationFirebase,
            firebaseAuth = firebaseAuth
        )
    }

    @Test
    fun `init should initial state`() {
        Assertions.assertEquals(
            settingsViewModel.settingsMutableStateFlow.value,
            SettingsState(
                generalSettings = listOf("Account Info", "Manage Declared Shots", "Terms & Conditions", "Using The App"),
                permissionSettings = listOf("Enabled Permissions", "View More Info"),
                debugSettings = listOf("Delete Account", "In-App Firebase Viewer", "Toggles")
            )
        )
    }

    @Test
    fun `general settings should return expected list`() {
        Assertions.assertEquals(
            settingsViewModel.generalSettings(),
            listOf("Account Info", "Manage Declared Shots", "Terms & Conditions", "Using The App")
        )
    }

    @Test
    fun `permission settings should return expected list`() {
        Assertions.assertEquals(
            settingsViewModel.permissionsSettings(),
            listOf("Enabled Permissions", "View More Info")
        )
    }

    @Nested
    inner class DebugSettings {

        @Test
        fun `when build type is debug should return expected list`() {
            every { application.getString(StringsIds.manageDeclaredShots) } returns "Manage Declared Shots"
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.settings) } returns "Settings"
            every { application.getString(StringsIds.accountInfo) } returns "Account Info"
            every { application.getString(StringsIds.termsConditions) } returns "Terms & Conditions"
            every { application.getString(StringsIds.usingTheApp) } returns "Using The App"
            every { application.getString(StringsIds.enabledPermissions) } returns "Enabled Permissions"
            every { application.getString(StringsIds.viewMoreInfo) } returns "View More Info"
            every { application.getString(StringsIds.settingsHelpDescription) } returns "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them."
            every { application.getString(StringsIds.debug) } returns "Debug"
            every { application.getString(StringsIds.deleteAccount) } returns "Delete Account"
            every { application.getString(StringsIds.inAppFirebaseViewer) } returns "In-App Firebase Viewer"
            every { application.getString(StringsIds.toggles) } returns "Toggles"

            settingsViewModel = SettingsViewModel(
                navigation = navigation,
                application = application,
                scope = scope,
                buildType = buildTypeDebug,
                activeUserRepository = activeUserRepository,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                accountManager = accountManager,
                authenticationFirebase = authenticationFirebase,
                firebaseAuth = firebaseAuth
            )

            val result = settingsViewModel.debugSettings()

            Assertions.assertEquals(result, listOf("Delete Account", "In-App Firebase Viewer", "Toggles"))
        }

        @Test
        fun `when build type is stage should return empty list`() {
            val emptyListStrings: List<String> = emptyList()

            every { application.getString(StringsIds.manageDeclaredShots) } returns "Manage Declared Shots"
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.settings) } returns "Settings"
            every { application.getString(StringsIds.accountInfo) } returns "Account Info"
            every { application.getString(StringsIds.termsConditions) } returns "Terms & Conditions"
            every { application.getString(StringsIds.usingTheApp) } returns "Using The App"
            every { application.getString(StringsIds.enabledPermissions) } returns "Enabled Permissions"
            every { application.getString(StringsIds.viewMoreInfo) } returns "View More Info"
            every { application.getString(StringsIds.settingsHelpDescription) } returns "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them."
            every { application.getString(StringsIds.debug) } returns "Debug"
            every { application.getString(StringsIds.deleteAccount) } returns "Delete Account"
            every { application.getString(StringsIds.inAppFirebaseViewer) } returns "In-App Firebase Viewer"
            every { application.getString(StringsIds.toggles) } returns "Toggles"

            settingsViewModel = SettingsViewModel(
                navigation = navigation,
                application = application,
                scope = scope,
                buildType = buildTypeStage,
                activeUserRepository = activeUserRepository,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                accountManager = accountManager,
                authenticationFirebase = authenticationFirebase,
                firebaseAuth = firebaseAuth
            )

            val result = settingsViewModel.debugSettings()

            Assertions.assertEquals(result, emptyListStrings)
        }

        @Test
        fun `when build type is release should return empty list`() {
            val emptyListStrings: List<String> = emptyList()

            every { application.getString(StringsIds.manageDeclaredShots) } returns "Manage Declared Shots"
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.settings) } returns "Settings"
            every { application.getString(StringsIds.accountInfo) } returns "Account Info"
            every { application.getString(StringsIds.termsConditions) } returns "Terms & Conditions"
            every { application.getString(StringsIds.usingTheApp) } returns "Using The App"
            every { application.getString(StringsIds.enabledPermissions) } returns "Enabled Permissions"
            every { application.getString(StringsIds.viewMoreInfo) } returns "View More Info"
            every { application.getString(StringsIds.settingsHelpDescription) } returns "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them."
            every { application.getString(StringsIds.debug) } returns "Debug"
            every { application.getString(StringsIds.deleteAccount) } returns "Delete Account"
            every { application.getString(StringsIds.inAppFirebaseViewer) } returns "In-App Firebase Viewer"
            every { application.getString(StringsIds.toggles) } returns "Toggles"

            settingsViewModel = SettingsViewModel(
                navigation = navigation,
                application = application,
                scope = scope,
                buildType = buildTypeRelease,
                activeUserRepository = activeUserRepository,
                deleteFirebaseUserInfo = deleteFirebaseUserInfo,
                accountManager = accountManager,
                authenticationFirebase = authenticationFirebase,
                firebaseAuth = firebaseAuth
            )

            val result = settingsViewModel.debugSettings()

            Assertions.assertEquals(result, emptyListStrings)
        }
    }

    @Test
    fun `on toolbar menu clicked`() {
        settingsViewModel.onToolbarMenuClicked()

        verify { navigation.openNavigationDrawer() }
    }

    @Test
    fun `on settings help alert`() {
        val alert = settingsViewModel.settingsHelpAlert()

        Assertions.assertEquals(
            alert,
            Alert(
                title = "Settings",
                description = "On the settings page, you can view your account information, review terms and conditions, access app usage details, and learn about the permissions we request and why we need them.",
                confirmButton = AlertConfirmAndDismissButton(buttonText = "Got It")
            )
        )
    }

    @Test
    fun `on help clicked`() {
        settingsViewModel.onHelpClicked()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class OnSettingItemClicked {

        @Test
        fun `when value passed in is using the app should navigate to onboarding education screen`() {
            settingsViewModel.onSettingItemClicked(value = "Using The App")

            verify { navigation.navigateToOnboardingEducationScreen() }
        }

        @Test
        fun `when value passed in is manage declared shots should navigate to manage declared shots`() {
            settingsViewModel.onSettingItemClicked(value = "Manage Declared Shots")

            verify { navigation.navigateToDeclaredShotsList() }
        }

        @Test
        fun `when value passed in is terms and conditions should navigate to terms and conditions screen`() {
            settingsViewModel.onSettingItemClicked(value = "Terms & Conditions")

            verify { navigation.navigateToTermsConditions() }
        }

        @Test
        fun `when value passed in is account info should navigate to account info screen`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns ActiveUser(
                id = 1,
                accountHasBeenCreated = false,
                username = "username",
                email = "email",
                firebaseAccountInfoKey = "key"
            )

            settingsViewModel.onSettingItemClicked(value = "Account Info")

            verify { navigation.navigateToAccountInfoScreen(username = "username", email = "email") }
        }

        @Test
        fun `when value passed in is enabled permissions should navigate to enabled permissions screen`() {
            settingsViewModel.onSettingItemClicked(value = "Enabled Permissions")

            verify { navigation.navigateToEnabledPermissions() }
        }

        @Test
        fun `when value passed in does not meet any conditions should navigate to permission education screen`() {
            settingsViewModel.onSettingItemClicked(value = "View Mode Info")

            verify { navigation.navigateToPermissionEducationScreen() }
        }

        @Test
        fun `when value passed in is delete account should show delete account alert`() {
            settingsViewModel.onSettingItemClicked(value = "Delete Account")

            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class FetchActiveUserAndNavigateToAccountInfo {

        @Test
        fun `when active user exists should navigate to account info screen with user data`() = runTest {
            val activeUser = ActiveUser(
                id = 1,
                accountHasBeenCreated = true,
                username = "testUser",
                email = "test@email.com",
                firebaseAccountInfoKey = "firebaseKey"
            )

            coEvery { activeUserRepository.fetchActiveUser() } returns activeUser

            settingsViewModel.fetchActiveUserAndNavigateToAccountInfo()

            verify { navigation.navigateToAccountInfoScreen(username = "testUser", email = "test@email.com") }
        }

        @Test
        fun `when active user is null should navigate to account info screen with empty strings`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns null

            settingsViewModel.fetchActiveUserAndNavigateToAccountInfo()

            verify { navigation.navigateToAccountInfoScreen(username = "", email = "") }
        }
    }

    @Nested
    inner class OnDeleteAccountYesClicked {

        @Test
        fun `when current user exists and both operations succeed should show success snackBar`() = runTest {
            val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
            every { firebaseAuth.currentUser } returns mockFirebaseUser
            every { mockFirebaseUser.uid } returns "testUid"
            every { authenticationFirebase.attemptToDeleteCurrentUserFlow(currentUser = mockFirebaseUser) } returns flowOf(true)
            every { deleteFirebaseUserInfo.deleteUser(uid = "testUid") } returns flowOf(true)

            settingsViewModel.onDeleteAccountYesClicked()

            verify { accountManager.logout() }
            verify { 
                navigation.snackBar(
                    snackBarInfo = SnackBarInfo(message = "Account has been deleted.")
                ) 
            }
        }

        @Test
        fun `when current user exists and auth succeeds but data deletion fails should show partial success  snackBar`() = runTest {
            val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
            every { firebaseAuth.currentUser } returns mockFirebaseUser
            every { mockFirebaseUser.uid } returns "testUid"
            every { authenticationFirebase.attemptToDeleteCurrentUserFlow(currentUser = mockFirebaseUser) } returns flowOf(true)
            every { deleteFirebaseUserInfo.deleteUser(uid = "testUid") } returns flowOf(false)

            settingsViewModel.onDeleteAccountYesClicked()

            verify { accountManager.logout() }
            verify { 
                navigation.snackBar(
                    snackBarInfo = SnackBarInfo(message = "Account has been deleted, but realtime database record could not be deleted.")
                ) 
            }
        }

        @Test
        fun `when current user exists and auth fails but data deletion succeeds should show partial success  snackBar`() = runTest {
            val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
            every { firebaseAuth.currentUser } returns mockFirebaseUser
            every { mockFirebaseUser.uid } returns "testUid"
            every { authenticationFirebase.attemptToDeleteCurrentUserFlow(currentUser = mockFirebaseUser) } returns flowOf(false)
            every { deleteFirebaseUserInfo.deleteUser(uid = "testUid") } returns flowOf(true)

            settingsViewModel.onDeleteAccountYesClicked()

            verify { accountManager.logout() }
            verify { 
                navigation.snackBar(
                    snackBarInfo = SnackBarInfo(message = "Account has not been deleted, but realtime database record got deleted.")
                ) 
            }
        }

        @Test
        fun `when current user exists and both operations fail should show failure snackBar`() = runTest {
            val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
            every { firebaseAuth.currentUser } returns mockFirebaseUser
            every { mockFirebaseUser.uid } returns "testUid"
            every { authenticationFirebase.attemptToDeleteCurrentUserFlow(currentUser = mockFirebaseUser) } returns flowOf(false)
            every { deleteFirebaseUserInfo.deleteUser(uid = "testUid") } returns flowOf(false)

            settingsViewModel.onDeleteAccountYesClicked()

            verify { accountManager.logout() }
            verify { 
                navigation.snackBar(
                    snackBarInfo = SnackBarInfo(message = "No data was actually deleted")
                ) 
            }
        }

        @Test
        fun `when current user is null should show user does not exist alert`() = runTest {
            every { firebaseAuth.currentUser } returns null

            settingsViewModel.onDeleteAccountYesClicked()

            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class AlertFunctions {

        @Test
        fun `delete account alert should return expected alert`() {
            val alert = settingsViewModel.deleteAccountAlert()

            Assertions.assertEquals("Delete Account", alert.title)
            Assertions.assertEquals("Are you sure you want to delete your account?", alert.description)
            Assertions.assertEquals("Yes", alert.confirmButton!!.buttonText)
            Assertions.assertEquals("No", alert.dismissButton!!.buttonText)
            Assertions.assertNotNull(alert.confirmButton!!.onButtonClicked)
        }

        @Test
        fun `user does not exist alert should return expected alert`() {
            val alert = settingsViewModel.userDoesNotExistAlert()

            Assertions.assertEquals(
                alert,
                Alert(
                    title = "Error",
                    description = "We could not delete your account because, user does not exist.",
                    confirmButton = AlertConfirmAndDismissButton(buttonText = "Got It")
                )
            )
        }
    }
}
