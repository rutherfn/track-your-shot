package com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.createreport

import android.app.Application
import android.net.Uri
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportNavigation
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportState
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportViewModel
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.dataadditionupdates.DataAdditionUpdates
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExt
import com.nicholas.rutherford.track.your.shot.helper.file.generator.PdfGenerator
import com.nicholas.rutherford.track.your.shot.notifications.Notifications
import io.mockk.coEvery
import io.mockk.coVerify
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
import java.util.Date

class CreateReportViewModelTest {

    private lateinit var createReportViewModel: CreateReportViewModel

    private val application = mockk<Application>(relaxed = true)
    private var navigation = mockk<CreateReportNavigation>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val notifications = mockk<Notifications>(relaxed = true)
    private val pdfGenerator = mockk<PdfGenerator>(relaxed = true)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)

    private val individualPlayerReportRepository = mockk<IndividualPlayerReportRepository>(relaxed = true)
    private val dataAdditionUpdates = mockk<DataAdditionUpdates>(relaxed = true)
    private val dateExt = mockk<DateExt>(relaxed = true)

    private val uri = mockk<Uri>(relaxed = true)
    private val defaultState = CreateReportState()

    @BeforeEach
    fun beforeEach() {
        createReportViewModel = CreateReportViewModel(
            application = application,
            navigation = navigation,
            playerRepository = playerRepository,
            scope = scope,
            notifications = notifications,
            pdfGenerator = pdfGenerator,
            createFirebaseUserInfo = createFirebaseUserInfo,
            individualPlayerReportRepository = individualPlayerReportRepository,
            dataAdditionUpdates = dataAdditionUpdates,
            dateExt = dateExt
        )
    }

    @Test
    fun `reset state should default state back`() {
        createReportViewModel.resetState()

        Assertions.assertEquals(createReportViewModel.createReportMutableStateFlow.value, defaultState)
    }

    @Test
    fun `on toolbar menu clicked should call pop`() {
        createReportViewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }

    @Nested
    inner class `update players state` {

        @Test
        fun `when player options returns empty should not update state`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns emptyList()

            createReportViewModel.updatePlayersState()

            val state = createReportViewModel.createReportMutableStateFlow.value

            Assertions.assertEquals(state, defaultState)
        }

        @Test
        fun `when player options returns info should update state`() = runTest {
            coEvery { playerRepository.fetchAllPlayers() } returns listOf(
                TestPlayer().create(),
                TestPlayer().create().copy(shotsLoggedList = emptyList())
            )

            createReportViewModel.updatePlayersState()

            val state = createReportViewModel.createReportMutableStateFlow.value

            Assertions.assertEquals(
                state,
                defaultState.copy(
                    playerOptions = listOf("first last"),
                    selectedPlayer = TestPlayer().create()
                )
            )
        }
    }

    @Test
    fun `cannot create pdf alert should return alert`() {
        every { application.getString(StringsIds.couldNotCreateReport) } returns "Could Not Create Report"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.couldNotGenerateTheReport) } returns "Unable to generate the report. Please try again or reach out to support for assistance."

        val alert = createReportViewModel.cannotCreatePdfAlert()

        Assertions.assertEquals(alert.title, "Could Not Create Report")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "Unable to generate the report. Please try again or reach out to support for assistance.")
    }

    @Test
    fun `cannot save pdf alert should return alert`() {
        every { application.getString(StringsIds.couldNotSaveReport) } returns "Could Not Save Report"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.couldNotGenerateTheReport) } returns "Unable to generate the report. Please try again or reach out to support for assistance."

        val alert = createReportViewModel.cannotSavePdfAlert()

        Assertions.assertEquals(alert.title, "Could Not Save Report")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "Unable to generate the report. Please try again or reach out to support for assistance.")
    }

    @Test
    fun `report generated for player should return alert`() {
        val playerName = "playerName"

        every { application.getString(StringsIds.playerReportCreatedForX, playerName) } returns "Player Report Created For playerName"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.playerReportCreatedDescription) } returns "A player report has been generated and saved to your device\'s download folder. You can view the report now or access it later in the app for future use."

        val alert = createReportViewModel.reportGeneratedForPlayer(playerName = playerName)

        Assertions.assertEquals(alert.title, "Player Report Created For playerName")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "A player report has been generated and saved to your device\'s download folder. You can view the report now or access it later in the app for future use.")
    }

    @Test
    fun `cannot upload pdf alert should return alert`() {
        every { application.getString(StringsIds.couldNotUploadReport) } returns "Could Not Upload Report"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.couldNotUploadReportDescription) } returns "Unable to upload the report for future use. Please try again or reach out to support for assistance."

        val alert = createReportViewModel.cannotUploadPdfAlert()

        Assertions.assertEquals(alert.title, "Could Not Upload Report")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "Unable to upload the report for future use. Please try again or reach out to support for assistance.")
    }

    @Nested
    inner class CreatePdfErrorAlert {

        @Test
        fun `when statusCode is equal to cannot create pdf code should return cannotCreatePdfAlert`() {
            every { application.getString(StringsIds.couldNotCreateReport) } returns "Could Not Create Report"
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.couldNotGenerateTheReport) } returns "Unable to generate the report. Please try again or reach out to support for assistance."

            val alert = createReportViewModel.createPdfErrorAlert(statusCode = Constants.PDF_CANNOT_CREATE_PDF_CODE)

            Assertions.assertEquals(alert.title, "Could Not Create Report")
            Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
            Assertions.assertEquals(alert.description, "Unable to generate the report. Please try again or reach out to support for assistance.")
        }

        @Test
        fun `when statusCode is not equal to cannot create pdf code should return cannotSavePdfAlert`() {
            every { application.getString(StringsIds.couldNotSaveReport) } returns "Could Not Save Report"
            every { application.getString(StringsIds.gotIt) } returns "Got It"
            every { application.getString(StringsIds.couldNotGenerateTheReport) } returns "Unable to generate the report. Please try again or reach out to support for assistance."

            val alert = createReportViewModel.createPdfErrorAlert(statusCode = Constants.PDF_SUCCESSFUL_GENERATE_CODE)

            Assertions.assertEquals(alert.title, "Could Not Save Report")
            Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
            Assertions.assertEquals(alert.description, "Unable to generate the report. Please try again or reach out to support for assistance.")
        }
    }

    @Test
    fun `store report should call create report and navigate to alert`() = runTest {
        coEvery { individualPlayerReportRepository.fetchReportCount() } returns 1

        createReportViewModel.storeReport(
            currentDateTime = Date().time,
            playerName = "player name",
            reportKey = "report key",
            pdfUrl = "pdf url"
        )

        coVerify { individualPlayerReportRepository.createReport(report = any()) }
        verify { navigation.disableProgress() }
        verify { navigation.alert(alert = any()) }
        verify { navigation.pop() }

        Assertions.assertEquals(createReportViewModel.createReportMutableStateFlow.value, defaultState)
    }

    @Nested
    inner class AttemptToUploadAndSaveReport {

        @Test
        fun `when attemptToCreatePdfFirebaseStorageResponseFlow returns null should call disable progress and alert`() = runTest {
            val uriString = "uriString"

            every { uri.toString() } returns uriString

            coEvery { createFirebaseUserInfo.attemptToCreatePdfFirebaseStorageResponseFlow(uri = uri) } returns flowOf(value = null)

            createReportViewModel.attemptToUploadAndSaveReport(
                uri = uri,
                fullName = "full name"
            )

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when attemptToCreatePdfFirebaseStorageResponseFlow returns value and other flow returns data but does not meet conditions should call disable progress and alert`() = runTest {
            val dateTime = Date().time
            val uriString = "uriString"
            val fullName = "fullName"
            val individualPlayerReportRealtimeResponse = IndividualPlayerReportRealtimeResponse(
                loggedDateValue = dateTime,
                playerName = fullName,
                pdfUrl = uriString
            )

            every { dateExt.now } returns dateTime
            every { uri.toString() } returns uriString

            coEvery { createFirebaseUserInfo.attemptToCreatePdfFirebaseStorageResponseFlow(uri = uri) } returns flowOf(value = uriString)
            coEvery {
                createFirebaseUserInfo.attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(
                    individualPlayerReportRealtimeResponse = individualPlayerReportRealtimeResponse
                )
            } returns flowOf(value = Pair(true, null))

            createReportViewModel.attemptToUploadAndSaveReport(
                uri = uri,
                fullName = fullName
            )

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when attemptToCreatePdfFirebaseStorageResponseFlow returns value and other flow returns data and meets conditions should call disable progress and alert`() = runTest {
            val dateTime = Date().time
            val uriString = "uriString"
            val fullName = "fullName"
            val individualPlayerReportRealtimeResponse = IndividualPlayerReportRealtimeResponse(
                loggedDateValue = dateTime,
                playerName = fullName,
                pdfUrl = uriString
            )
            val key = "key"

            every { dateExt.now } returns dateTime
            every { uri.toString() } returns uriString

            coEvery { createFirebaseUserInfo.attemptToCreatePdfFirebaseStorageResponseFlow(uri = uri) } returns flowOf(value = uriString)
            coEvery {
                createFirebaseUserInfo.attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(
                    individualPlayerReportRealtimeResponse = individualPlayerReportRealtimeResponse
                )
            } returns flowOf(value = Pair(true, key))

            createReportViewModel.attemptToUploadAndSaveReport(
                uri = uri,
                fullName = fullName
            )

            coVerify {
                createReportViewModel.storeReport(
                    currentDateTime = dateTime,
                    playerName = fullName,
                    reportKey = key,
                    pdfUrl = uriString
                )
            }

            coVerify { individualPlayerReportRepository.createReport(report = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
            verify { navigation.pop() }

            Assertions.assertEquals(createReportViewModel.createReportMutableStateFlow.value, defaultState)
        }
    }

    @Nested
    inner class AttemptToGeneratePlayerReport {
        @Test
        fun `when selected player of state returns null should show disable progress and alert`() {
            createReportViewModel.createReportMutableStateFlow.value = CreateReportState(selectedPlayer = null)

            createReportViewModel.attemptToGeneratePlayerReport()

            verify { navigation.enableProgress(progress = any()) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        // todo -> Figure out a way to test if the uri comes back as null or a value for this function need to do research on how to do that
    }
}
