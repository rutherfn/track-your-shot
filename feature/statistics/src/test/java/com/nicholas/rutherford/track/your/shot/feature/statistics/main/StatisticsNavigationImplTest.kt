package com.nicholas.rutherford.track.your.shot.feature.statistics.main

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StatisticsNavigationImplTest {

    private lateinit var statisticsNavigationImpl: StatisticsNavigationImpl

    private var navigator = mockk<Navigator>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        statisticsNavigationImpl = StatisticsNavigationImpl(navigator = navigator)
    }

    @Test
    fun `alert action`() {
        val alert = Alert(title = "Title")

        statisticsNavigationImpl.alert(alert = alert)

        verify { navigator.alert(alertAction = alert) }
    }

    @Test
    fun `open navigation drawer`() {
        statisticsNavigationImpl.openNavigationDrawer()

        verify { navigator.showNavigationDrawer(navigationDrawerAction = true) }
    }

    @Test
    fun `navigate to player statistics`() {
        statisticsNavigationImpl.navigateToPlayerStatistics(playerId = 1)

        verify(exactly = 1) { navigator.navigate(navigationAction = any()) }
    }
}
