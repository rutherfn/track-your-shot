package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.net.Uri
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * Represents the UI state for the Create/Edit Player screen.
 *
 * @property firstName The player's first name input by the user. Defaults to an empty string.
 * @property lastName The player's last name input by the user. Defaults to an empty string.
 * @property editedPlayerUrl The URL of the player's image when editing an existing player. Defaults to an empty string.
 * @property toolbarNameResId Resource ID for the toolbar title, indicating whether the screen is for creating or editing a player.
 *                         Defaults to the string resource for "Create Player".
 * @property playerPositionString The selected player position as a string (e.g., "Point Guard").
 * @property hintLogNewShotText Hint text shown in the UI for logging a new shot, dynamically updated based on the player's name.
 * @property shots List of confirmed shots logged for the player.
 * @property pendingShots List of shots that are pending confirmation or upload.
 * @property sheet Optional bottom sheet UI model to display additional options or dialogs.
 * @property imageUri Optional URI for the selected or captured player image.
 */
data class CreateEditPlayerState(
    val firstName: String = "",
    val lastName: String = "",
    val editedPlayerUrl: String = "",
    val toolbarNameResId: Int = StringsIds.createPlayer,
    val playerPositionString: String = "",
    val hintLogNewShotText: String = "",
    val shots: List<ShotLogged> = emptyList(),
    val pendingShots: List<ShotLogged> = emptyList(),
    val sheet: Sheet? = null,
    val imageUri: Uri? = null
)
