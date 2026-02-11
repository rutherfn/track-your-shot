package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.data.room.converters.HasShotsLoggedFilterConverter
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerFilterPositionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Domain model representing a player filter with all filter criteria.
 *
 * @property hasShotsLogged Filter for players with shots logged. null = no filter, HasShots = has shots, NoShots = no shots, Both = all players.
 * @property minShots Minimum number of shots for filtering. null = no minimum.
 * @property maxShots Maximum number of shots for filtering. null = no maximum.
 * @property selectedPositions List of player positions to filter by. Empty list = no position filter.
 * @property lastUpdatedValue Timestamp in String for when the filter was last updated.
 */
data class PlayerFilter(
    val hasShotsLogged: HasShotsLoggedFilter? = null,
    val minShots: Int? = null,
    val maxShots: Int? = null,
    val selectedPositions: List<String> = emptyList(),
    val lastUpdatedValue: String = ""
)

/**
 * Converts a [PlayerFilterEntity] and list of positions to a [PlayerFilter] domain model.
 * It will also filter the last provided last updated date value
 *
 * @param positions List of position strings from PlayerFilterPositionEntity.
 * @return A [PlayerFilter] instance with values mapped from the entity and positions.
 */
fun PlayerFilterEntity.toPlayerFilter(positions: List<String>): PlayerFilter {
    val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
    return PlayerFilter(
        hasShotsLogged = HasShotsLoggedFilter.fromValue(hasShotsLogged).takeIf { it !is HasShotsLoggedFilter.None },
        minShots = minShots,
        maxShots = maxShots,
        selectedPositions = positions.sorted(),
        lastUpdatedValue = formatter.format(Date(lastUpdated))
    )
}

/**
 * Calculates the total count of active filters.
 *
 * Count includes:
 * - Each selected position (1 per position)
 * - hasShotsLogged filter (1 if set, but excludes "Both" as it's the default state)
 * - minShots filter (1 if set)
 * - maxShots filter (1 if set)
 *
 * @return The total count of active filters.
 */
fun PlayerFilter.getFilterCount(): Int {
    var count = 0

    // Count selected positions
    count += selectedPositions.size

    // Count hasShotsLogged filter (1 if not null, not None, and not Both)
    // "Both" is the default state and should not count as a filter
    if (hasShotsLogged != null && hasShotsLogged !is HasShotsLoggedFilter.None && hasShotsLogged !is HasShotsLoggedFilter.Both) {
        count += 1
    }

    // Count minShots filter (1 if not null)
    if (minShots != null) {
        count += 1
    }

    // Count maxShots filter (1 if not null)
    if (maxShots != null) {
        count += 1
    }

    return count
}

/**
 * Converts a [PlayerFilter] domain model to a [PlayerFilterEntity] and list of [PlayerFilterPositionEntity].
 *
 * @return Pair containing the [PlayerFilterEntity] and list of [PlayerFilterPositionEntity].
 */
fun PlayerFilter.toPlayerFilterEntities(): Pair<PlayerFilterEntity, List<PlayerFilterPositionEntity>> {
    val converter = HasShotsLoggedFilterConverter()
    val filterEntity = PlayerFilterEntity(
        id = 1,
        hasShotsLogged = converter.toValue(hasShotsLogged),
        minShots = minShots,
        maxShots = maxShots,
        lastUpdated = System.currentTimeMillis()
    )

    val positionEntities = selectedPositions.sorted().map { position ->
        PlayerFilterPositionEntity(
            filterId = 1,
            position = position
        )
    }

    return Pair(filterEntity, positionEntities)
}
