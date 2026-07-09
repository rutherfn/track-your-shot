package com.nicholas.rutherford.track.your.shot.feature.statistics

import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2026-07-05
 *
 * Builds aggregated shooting statistics for this player.
 *
 * Pending shots are excluded from the summary.
 *
 * @return A [PlayerStatisticsSummary] containing totals and overall shooting percentage.
 */
fun Player.toPlayerStatisticsSummary(): PlayerStatisticsSummary {
    val finalizedShots = shotsLoggedList.filterNot { shotLogged -> shotLogged.isPending }
    val totalAttempted = finalizedShots.sumOf { shotLogged -> shotLogged.shotsAttempted }
    val totalMade = finalizedShots.sumOf { shotLogged -> shotLogged.shotsMade }
    val totalMissed = finalizedShots.sumOf { shotLogged -> shotLogged.shotsMissed }
    val overallMadePercentage = if (totalAttempted > 0) {
        (totalMade.toDouble() / totalAttempted.toDouble()) * Constants.PERCENTAGE_MULTIPLIER
    } else {
        0.0
    }

    return PlayerStatisticsSummary(
        playerId = requireNotNull(id) { "Player id is required to build statistics summary." },
        playerName = fullName(),
        totalShotsAttempted = totalAttempted,
        totalShotsMade = totalMade,
        totalShotsMissed = totalMissed,
        overallMadePercentage = overallMadePercentage,
        loggedShotsCount = finalizedShots.size
    )
}
