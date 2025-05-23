package com.nicholas.rutherford.track.your.shot.helper.account

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.entities.toActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.entities.toPlayer
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUserEntity
import com.nicholas.rutherford.track.your.shot.data.test.room.TestDeclaredShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestAccountInfoRealTimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestDeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestIndividualPlayerReportWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import io.mockk.*
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

@OptIn(ExperimentalCoroutinesApi::class)
class AccountManagerImplTest {

    private lateinit var accountManagerImpl: AccountManagerImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val application = mockk<Application>(relaxed = true)

    private val navigator = mockk<Navigator>(relaxed = true)

    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)
    private val declaredShotRepository = mockk<DeclaredShotRepository>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val individualPlayerReportRepository = mockk<IndividualPlayerReportRepository>(relaxed = true)
    private val pendingPlayerRepository = mockk<PendingPlayerRepository>(relaxed = true)
    private val shotIgnoringRepository = mockk<ShotIgnoringRepository>(relaxed = true)

    private val userRepository = mockk<UserRepository>(relaxed = true)

    private val readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)
    private val existingUserFirebase = mockk<ExistingUserFirebase>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)

    private val accountInfoRealtimeResponse = TestAccountInfoRealTimeResponse().create()
    private val password = "Password$1"

    @BeforeEach
    fun beforeEach() {
        accountManagerImpl = AccountManagerImpl(
            scope = scope,
            application = application,
            navigator = navigator,
            activeUserRepository = activeUserRepository,
            declaredShotRepository = declaredShotRepository,
            playerRepository = playerRepository,
            individualPlayerReportRepository = individualPlayerReportRepository,
            pendingPlayerRepository = pendingPlayerRepository,
            shotIgnoringRepository = shotIgnoringRepository,
            userRepository = userRepository,
            readFirebaseUserInfo = readFirebaseUserInfo,
            existingUserFirebase = existingUserFirebase,
            createSharedPreferences = createSharedPreferences
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `logout verify should call functions in order`() = runTest {
        accountManagerImpl.logout()

        coVerifyOrder {
            navigator.progress(progressAction = any())
            existingUserFirebase.logout()
            createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = false)
            createSharedPreferences.createHasAuthenticatedAccount(value = false)
            createSharedPreferences.createIsLoggedIn(value = false)
            accountManagerImpl.clearOutDatabase()
        }

        testDispatcher.scheduler.apply { advanceTimeBy(9000); runCurrent() }

        verifyOrder {
            navigator.progress(progressAction = any())
            navigator.navigate(navigationAction = any())
        }

        Assertions.assertEquals(accountManagerImpl.declaredShotIds, emptyList<Int>())
    }

    @Test
    fun `clear out database verify should call delete functions`() = runTest {
        accountManagerImpl.clearOutDatabase()

        coVerifyOrder {
            activeUserRepository.deleteActiveUser()
            playerRepository.deleteAllPlayers()
            pendingPlayerRepository.deleteAllPendingPlayers()
            userRepository.deleteAllUsers()
            declaredShotRepository.deleteAllDeclaredShots()
            shotIgnoringRepository.deleteAllShotsIgnoring()
        }
    }

    @Nested
    inner class Login {
        @Test
        fun `when loginFlow returns as not successful should call disableProgressAndShowUnableToLoginAlert`() {
            coEvery { existingUserFirebase.loginFlow(email = accountInfoRealtimeResponse.email, password = password) } returns flowOf(value = false)
            accountManagerImpl.login(email = accountInfoRealtimeResponse.email, password = password)

            verify { navigator.progress(progressAction = any()) }
            coVerify { accountManagerImpl.disableProgressAndShowUnableToLoginAlert() }
        }

        @Test
        fun `when loginFlow returns as successful and get account info flow by email returns null should call disableProgressAndShowUnableToLoginAlert`() {
            coEvery { existingUserFirebase.loginFlow(email = accountInfoRealtimeResponse.email, password = password) } returns flowOf(value = true)
            coEvery { readFirebaseUserInfo.getAccountInfoFlow() } returns flowOf(value = null)
            accountManagerImpl.login(email = accountInfoRealtimeResponse.email, password = password)

            verify { navigator.progress(progressAction = any()) }
            coVerify { accountManagerImpl.disableProgressAndShowUnableToLoginAlert() }
        }

        @Test
        fun `when loginFlow returns as successful and get account info flow by email returns info should not call disableProgressAndShowUnableToLoginAlert`() {
            coEvery { existingUserFirebase.loginFlow(email = accountInfoRealtimeResponse.email, password = password) } returns flowOf(value = true)
            coEvery { readFirebaseUserInfo.getAccountInfoFlow() } returns flowOf(value = accountInfoRealtimeResponse)
            accountManagerImpl.login(email = accountInfoRealtimeResponse.email, password = password)

            verify { navigator.progress(progressAction = any()) }

            coVerify(exactly = 0) { accountManagerImpl.disableProgressAndShowUnableToLoginAlert() }
        }
    }

    @Test
    fun `deleteAllPendingPlayers should call repository delete all pending players`() {
        accountManagerImpl.deleteAllPendingPlayers()

        coVerify(exactly = 1) { pendingPlayerRepository.deleteAllPendingPlayers() }
    }

    @Nested
    inner class DeleteAllPendingShotsFromPlayers {

        @Test
        fun `when fetchAllPlayers return empty should not call updatePlayer`() {
            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()

            accountManagerImpl.deleteAllPendingShotsFromPlayers()

            coVerify(exactly = 0) { playerRepository.updatePlayer(currentPlayer = any(), newPlayer = any()) }
        }

        @Test
        fun `when fetchAllPlayers returns players but empty shots should not call updatePlayer`() {
            val players = listOf(TestPlayerEntity().create().copy(shotsLoggedList = emptyList()).toPlayer())

            coEvery { playerRepository.fetchAllPlayers() } returns players

            accountManagerImpl.deleteAllPendingShotsFromPlayers()

            coVerify(exactly = 0) { playerRepository.updatePlayer(currentPlayer = any(), newPlayer = any()) }
        }

        @Test
        fun `when fetchAllPlayers returns players with no pending shots should not call updatePlayer`() {
            val players = listOf(TestPlayerEntity().create().toPlayer())

            coEvery { playerRepository.fetchAllPlayers() } returns players

            accountManagerImpl.deleteAllPendingShotsFromPlayers()

            coVerify(exactly = 0) { playerRepository.updatePlayer(currentPlayer = any(), newPlayer = any()) }
        }

        @Test
        fun `when fetchAllPlayers returns player with pending shots should call updatePlayer`() {
            val shotsLoggedList = listOf(
                TestShotLogged.build().copy(isPending = false),
                TestShotLogged.build().copy(
                    shotType = 2,
                    shotsAttempted = 6,
                    shotsMade = 3,
                    shotsMissed = 3,
                    shotsMadePercentValue = 50.0,
                    shotsMissedPercentValue = 50.0,
                    shotsAttemptedMillisecondsValue = 2000L,
                    shotsLoggedMillisecondsValue = 4000L,
                    isPending = true
                )
            )
            val players = listOf(TestPlayerEntity().create().copy(shotsLoggedList = shotsLoggedList).toPlayer())

            coEvery { playerRepository.fetchAllPlayers() } returns players

            accountManagerImpl.deleteAllPendingShotsFromPlayers()

            val currentPlayer = players[0]
            val newPlayer = TestPlayerEntity().create().copy(shotsLoggedList = listOf(TestShotLogged.build().copy(isPending = false))).toPlayer()

            coVerify(exactly = 1) { playerRepository.updatePlayer(currentPlayer = currentPlayer, newPlayer = newPlayer) }
        }
    }

    @Nested
    inner class CheckForActiveUsersAndPlayers {
        private val activeUserEntity = TestActiveUserEntity().create().copy()
        private val playerEntityList = listOf(TestPlayerEntity().create())

        @Test
        fun `when fetch active user is not null should call delete all active users`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns activeUserEntity.toActiveUser()

            accountManagerImpl.checkForActiveUserAndPlayers()

            coVerify { activeUserRepository.deleteActiveUser() }
        }

        @Test
        fun `when fetch active user is not null should not call delete all active users`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns null

            accountManagerImpl.checkForActiveUserAndPlayers()

            coVerify(exactly = 0) { activeUserRepository.deleteActiveUser() }
        }

        @Test
        fun `when fetch all players is not empty should call delete all players`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns playerEntityList.map { it.toPlayer() }

            accountManagerImpl.checkForActiveUserAndPlayers()

            coVerify { playerRepository.deleteAllPlayers() }
        }

        @Test
        fun `when fetch all players is empty should not call delete all players`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()

            accountManagerImpl.checkForActiveUserAndPlayers()

            coVerify(exactly = 0) { playerRepository.deleteAllPlayers() }
        }
    }

    @Nested
    inner class UpdateActiveUserFromLoggedInUser {
        private val key = "key"
        private val activeUserEntity = TestActiveUserEntity().create().copy()
        private val playerInfoRealtimeWithKeyResponseList = listOf(TestPlayerInfoRealtimeWithKeyResponse().create())

        @Test
        fun `when getAccountInfoKeyFlowByEmail returns null should call disableProgressAndShowUnableToLoginAlert`() = runTest {
            coEvery { readFirebaseUserInfo.getAccountInfoKeyFlow() } returns flowOf(value = null)

            accountManagerImpl.updateActiveUserFromLoggedInUser(email = accountInfoRealtimeResponse.email, username = accountInfoRealtimeResponse.userName)

            coVerify { accountManagerImpl.disableProgressAndShowUnableToLoginAlert(isLoggedIn = true) }
        }

        @Test
        fun `when getAccountInfoKeyFlowByEmail returns value and fetchActiveUser returns info should call delete active user`() = runTest {
            coEvery { readFirebaseUserInfo.getAccountInfoKeyFlow() } returns flowOf(value = key)
            coEvery { activeUserRepository.fetchActiveUser() } returns activeUserEntity.toActiveUser()

            accountManagerImpl.updateActiveUserFromLoggedInUser(email = activeUserEntity.email, username = activeUserEntity.username)

            coVerify {
                activeUserRepository.createActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        username = activeUserEntity.username,
                        email = activeUserEntity.email,
                        firebaseAccountInfoKey = key
                    )
                )
            }
            coVerify { activeUserRepository.deleteActiveUser() }
        }

        @Test
        fun `when getAccountInfoKeyFlowByEmail returns value, fetchActiveUser returns null, when get playerInfoList returns empty should not call create list of players`() = runTest {
            val declaredShotList = listOf(TestDeclaredShot.build())

            coEvery { readFirebaseUserInfo.getAccountInfoKeyFlow() } returns flowOf(value = key)
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns declaredShotList
            coEvery { readFirebaseUserInfo.getPlayerInfoList() } returns flowOf(emptyList())

            accountManagerImpl.updateActiveUserFromLoggedInUser(email = activeUserEntity.email, username = activeUserEntity.username)

            coVerify {
                activeUserRepository.createActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        username = activeUserEntity.username,
                        email = activeUserEntity.email,
                        firebaseAccountInfoKey = key
                    )
                )
            }
            coVerify(exactly = 0) { playerRepository.createListOfPlayers(playerList = any()) }
        }

        @Test
        fun `when getAccountInfoKeyFlowByEmail returns value, fetchActiveUser returns null, when get playerInfoList returns data should call create list of players`() = runTest {
            val declaredShotList = listOf(TestDeclaredShot.build())

            coEvery { readFirebaseUserInfo.getAccountInfoKeyFlow() } returns flowOf(value = key)
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { declaredShotRepository.fetchAllDeclaredShots() } returns declaredShotList
            coEvery { readFirebaseUserInfo.getPlayerInfoList() } returns flowOf(playerInfoRealtimeWithKeyResponseList)

            accountManagerImpl.updateActiveUserFromLoggedInUser(email = activeUserEntity.email, username = activeUserEntity.username)

            coVerify {
                activeUserRepository.createActiveUser(
                    activeUser = ActiveUser(
                        id = Constants.ACTIVE_USER_ID,
                        accountHasBeenCreated = true,
                        username = activeUserEntity.username,
                        email = activeUserEntity.email,
                        firebaseAccountInfoKey = key
                    )
                )
            }
            coVerify(exactly = 0) { activeUserRepository.deleteActiveUser() }
            coVerify { playerRepository.createListOfPlayers(playerList = any()) }
        }
    }

    @Nested
    inner class CollectPlayerInfoList {
        @Test
        fun `when getPlayerInfoList returns empty list should not createListOfPlayers`() = runTest {
            coEvery { readFirebaseUserInfo.getPlayerInfoList() } returns flowOf(emptyList())

            accountManagerImpl.collectPlayerInfoList()

            coVerify(exactly = 0) { playerRepository.createListOfPlayers(playerList = any()) }
        }

        @Test
        fun `when getPlayerInfoList returns list of info should call createListOfPlayers`() = runTest {
            val playerArrayList: ArrayList<Player> = arrayListOf()
            val playerInfoRealtimeWithKeyResponseList = listOf(TestPlayerInfoRealtimeWithKeyResponse().create())

            playerInfoRealtimeWithKeyResponseList.map { player ->
                playerArrayList.add(
                    Player(
                        firstName = player.playerInfo.firstName,
                        lastName = player.playerInfo.lastName,
                        position = PlayerPositions.fromValue(player.playerInfo.positionValue),
                        firebaseKey = player.playerFirebaseKey,
                        imageUrl = player.playerInfo.imageUrl,
                        shotsLoggedList = player.playerInfo.shotsLogged.map { shotLoggedRealtimeResponse ->
                            ShotLogged(
                                id = shotLoggedRealtimeResponse.id,
                                shotName = shotLoggedRealtimeResponse.shotName,
                                shotType = shotLoggedRealtimeResponse.shotType,
                                shotsAttempted = shotLoggedRealtimeResponse.shotsAttempted,
                                shotsMade = shotLoggedRealtimeResponse.shotsMade,
                                shotsMissed = shotLoggedRealtimeResponse.shotsMissed,
                                shotsMadePercentValue = shotLoggedRealtimeResponse.shotsMadePercentValue,
                                shotsMissedPercentValue = shotLoggedRealtimeResponse.shotsMissedPercentValue,
                                shotsAttemptedMillisecondsValue = shotLoggedRealtimeResponse.shotsAttemptedMillisecondsValue,
                                shotsLoggedMillisecondsValue = shotLoggedRealtimeResponse.shotsLoggedMillisecondsValue,
                                isPending = shotLoggedRealtimeResponse.isPending
                            )
                        }
                    )
                )
            }

            coEvery { readFirebaseUserInfo.getPlayerInfoList() } returns flowOf(playerInfoRealtimeWithKeyResponseList)

            accountManagerImpl.collectPlayerInfoList()

            coVerify { playerRepository.createListOfPlayers(playerList = any()) }
        }
    }

    @Nested
    inner class CollectReportList {

        @Test
        fun `when getReportList returns back empty list should not update database`() = runTest {
            coEvery { readFirebaseUserInfo.getReportList() } returns flowOf(value = emptyList())

            accountManagerImpl.collectReportList()

            coVerify(exactly = 0) { individualPlayerReportRepository.createReports(individualPlayerReports = any()) }
        }

        @Test
        fun `when getReportList returns back non empty list should update database`() = runTest {
            val report = TestIndividualPlayerReportWithKeyRealtimeResponse().create()
            val key = "key-1"
            val individualPlayerReportWithKeyRealtimeResponseList = listOf(
                IndividualPlayerReportWithKeyRealtimeResponse(
                    reportFirebaseKey = key,
                    IndividualPlayerReportRealtimeResponse(
                        loggedDateValue = report.playerReport.loggedDateValue,
                        playerName = report.playerReport.playerName,
                        pdfUrl = report.playerReport.pdfUrl
                    )
                )
            )

            coEvery { readFirebaseUserInfo.getReportList() } returns flowOf(value = individualPlayerReportWithKeyRealtimeResponseList)

            accountManagerImpl.collectReportList()

            coVerify(exactly = 1) { individualPlayerReportRepository.createReports(individualPlayerReports = any()) }
        }
    }

    @Nested
    inner class CollectDeclaredShotIds {

        @Test
        fun `when getDeletedShotIdsFlow returns empty list should not create shot ignoring or update field`() = runTest {
            coEvery { readFirebaseUserInfo.getDeletedShotIdsFlow() } returns flowOf(value = emptyList())

            accountManagerImpl.collectDeletedShotIds()

            Assertions.assertEquals(accountManagerImpl.declaredShotIds, emptyList<Int>())
            coVerify(exactly = 0) { shotIgnoringRepository.createShotIgnoring(shotId = 1) }
            coVerify(exactly = 0) { shotIgnoringRepository.createShotIgnoring(shotId = 2) }
        }

        @Test
        fun `when getDeletedShotIdsFlow returns non empty list should not create shot ignoring or update field`() = runTest {
            val ids = listOf(1, 2)

            coEvery { readFirebaseUserInfo.getDeletedShotIdsFlow() } returns flowOf(value = ids)

            accountManagerImpl.collectDeletedShotIds()

            Assertions.assertEquals(accountManagerImpl.declaredShotIds, ids)
            coVerify(exactly = 1) { shotIgnoringRepository.createShotIgnoring(shotId = 1) }
            coVerify(exactly = 1) { shotIgnoringRepository.createShotIgnoring(shotId = 2) }
        }
    }

    @Nested
    inner class CollectDeclaredShots {
        val shotIds = listOf(3, 4, 5)
        val declaredShotWithKeyRealtimeResponse = TestDeclaredShotWithKeyRealtimeResponse.create()

        @Test
        fun `when getCreatedDeclaredShotsFlow returns empty should call functions`() = runTest {
            accountManagerImpl.declaredShotIds = shotIds
            coEvery { readFirebaseUserInfo.getCreatedDeclaredShotsFlow() } returns flowOf(emptyList())

            accountManagerImpl.collectDeclaredShots()

            coVerify(exactly = 0) {
                declaredShotRepository.createNewDeclaredShot(
                    declaredShot = DeclaredShot(
                        id = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                        shotCategory = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                        title = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                        description = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description,
                        firebaseKey = declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey
                    )
                )
            }

            coVerifyOrder {
                createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(true)
                declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = shotIds)

                createSharedPreferences.createHasAuthenticatedAccount(true)
                createSharedPreferences.createIsLoggedIn(true)
                navigator.progress(null)
                navigator.navigate(any())
            }
        }

        @Test
        fun `when getCreatedDeclaredShotsFlow return non empty list and does contain declaredShotIds should call functions`() = runTest {
            accountManagerImpl.declaredShotIds = listOf(1)
            coEvery { readFirebaseUserInfo.getCreatedDeclaredShotsFlow() } returns flowOf(listOf(declaredShotWithKeyRealtimeResponse))

            accountManagerImpl.collectDeclaredShots()

            coVerify(exactly = 0) {
                declaredShotRepository.createNewDeclaredShot(
                    declaredShot = DeclaredShot(
                        id = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                        shotCategory = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                        title = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                        description = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description,
                        firebaseKey = declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey
                    )
                )
            }

            coVerifyOrder {
                createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(true)
                declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = listOf(1))

                createSharedPreferences.createHasAuthenticatedAccount(true)
                createSharedPreferences.createIsLoggedIn(true)
                navigator.progress(null)
                navigator.navigate(any())
            }
        }

        @Test
        fun `when getCreatedDeclaredShotsFlow return non empty list and does not contain declaredShotIds should call functions`() = runTest {
            accountManagerImpl.declaredShotIds = shotIds
            coEvery { readFirebaseUserInfo.getCreatedDeclaredShotsFlow() } returns flowOf(listOf(declaredShotWithKeyRealtimeResponse))

            accountManagerImpl.collectDeclaredShots()

            coVerifyOrder {
                declaredShotRepository.createNewDeclaredShot(
                    declaredShot = DeclaredShot(
                        id = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                        shotCategory = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                        title = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                        description = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description,
                        firebaseKey = declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey
                    )
                )
                createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(true)
                declaredShotRepository.createDeclaredShots(shotIdsToFilterOut = shotIds)

                createSharedPreferences.createHasAuthenticatedAccount(true)
                createSharedPreferences.createIsLoggedIn(true)
                navigator.progress(null)
                navigator.navigate(any())
            }
        }
    }

    @Nested
    inner class DisableProgressAndShowUnableToLoginAlert {

        @Test
        fun `when isLoggedIn is set to true should log out and clear database, and call unable to login alert`() = runTest {
            accountManagerImpl.disableProgressAndShowUnableToLoginAlert(isLoggedIn = true)

            verify { existingUserFirebase.logout() }
            coVerify { accountManagerImpl.clearOutDatabase() }

            verifyOrder {
                navigator.progress(progressAction = null)
                navigator.alert(alertAction = any())
                navigator.alert(alertAction = any())
            }
        }

        @Test
        fun `when isLoggedIn is set to false should not log out and clear database, and call unable to login alert`() = runTest {
            accountManagerImpl.disableProgressAndShowUnableToLoginAlert(isLoggedIn = false)

            verify(exactly = 0) { existingUserFirebase.logout() }
            coVerify(exactly = 0) { accountManagerImpl.clearOutDatabase() }

            verifyOrder {
                navigator.progress(progressAction = null)
                navigator.alert(alertAction = any())
                navigator.alert(alertAction = any())
            }
        }
    }

    @Test
    fun `unableToLoginAccountAlert should have valid values`() {
        Assertions.assertEquals(
            accountManagerImpl.unableToLoginToAccountAlert().title,
            application.getString(StringsIds.unableToLoginToAccount)
        )
        Assertions.assertEquals(
            accountManagerImpl.unableToLoginToAccountAlert().description,
            application.getString(StringsIds.havingTroubleLoggingIntoYourAccountPleaseTryAgainAndEnsureCredentialsExistAndAreValid)
        )
        Assertions.assertEquals(
            accountManagerImpl.unableToLoginToAccountAlert().dismissButton!!.buttonText,
            application.getString(StringsIds.gotIt)
        )
    }
}
