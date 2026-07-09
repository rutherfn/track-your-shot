package com.nicholas.rutherford.track.your.shot.feature.statistics

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.feature.statistics.main.StatisticsNavigation
import com.nicholas.rutherford.track.your.shot.feature.statistics.main.StatisticsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.BeforeEach

class StatisticsViewModelTest {

    private lateinit var viewModel: StatisticsViewModel

    private val application = mockk<Application>(relaxed = true)
    private val navigation = mockk<StatisticsNavigation>(relaxed = true)
    private val playerRepository = mockk<PlayerRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.statistics) } returns "Statistics"
        every { application.getString(StringsIds.all) } returns "All"
        every { application.getString(StringsIds.trackYourProgressDescription) } returns "Track your progress description"
        every { application.getString(StringsIds.gotIt) } returns "Got it"

        viewModel = StatisticsViewModel(
            application = application,
            scope = scope,
            navigation = navigation,
            playerRepository = playerRepository
        )
    }
}
