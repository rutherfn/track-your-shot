package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.Center.toPlayerPosition
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerState
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.flow.Flow

/**
 * Helper class containing logic specific to editing existing players.
 *
 * This class provides functions that are only used when editing an existing player, such as:
 * - Checking if changes have been made to the player
 * - Updating a player in Firebase
 * - Updating a player in the local Room database
 * - Processing deleted shots
 * - Updating state for an existing player
 *
 * All functions are documented with their use cases and expected behavior.
 */
class EditPlayerHelper(
    private val application: Application,
    private val playerRepository: PlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo
) {

    /**
     * Checks if there were any edits made to the existing player.
     *
     * This function compares the current state with the existing player to determine
     * if any changes have been made. It checks:
     * - First name changes
     * - Last name changes
     * - Position changes
     * - Image changes (new URI or removed image)
     * - Pending shots (if any pending shots exist, changes are detected)
     *
     * Use cases:
     * - Before saving to prevent unnecessary updates
     * - To show "no changes" alert if nothing was modified
     * - As part of the validation flow before updating
     *
     * @param existingPlayer The original player being edited.
     * @param uri The new image URI, if a new image was selected.
     * @param state The current screen state with potential changes.
     * @param pendingShotLoggedList List of pending shots that indicate changes.
     * @return true if no changes were made, false if changes exist.
     */
    fun hasNotEditedExistingPlayer(
        existingPlayer: Player,
        uri: android.net.Uri?,
        state: CreateEditPlayerState,
        pendingShotLoggedList: List<PendingShot>
    ): Boolean {
        val hasSameName =
            existingPlayer.firstName == state.firstName && existingPlayer.lastName == state.lastName
        val hasSamePosition =
            application.getString(existingPlayer.position.toType()) == state.playerPositionString
        val hasSamePlacedImage = if (existingPlayer.imageUrl == null) {
            false
        } else if (uri != null) {
            false
        } else {
            state.editedPlayerUrl == existingPlayer.imageUrl
        }
        return hasSameName && hasSamePosition && hasSamePlacedImage && pendingShotLoggedList.isEmpty()
    }

    /**
     * Updates an existing user in Firebase with the new state and image URL.
     *
     * This function updates the player information in Firebase Realtime Database.
     * It requires both the active user's Firebase key and the player's Firebase key.
     *
     * Use cases:
     * - When saving changes to an existing player
     * - When syncing player updates to Firebase
     * - As part of the edit player workflow
     *
     * @param player The existing player being edited.
     * @param state The current screen state with updated information.
     * @param imageUrl The Firebase Storage URL for the player's image, or null if no image.
     * @param shotsLoggedRealtimeResponseList List of shots in Firebase realtime response format.
     * @return Flow that emits true if update was successful, false otherwise.
     * @throws IllegalStateException if required keys are missing.
     */
    suspend fun updateUserInFirebase(
        player: Player,
        state: CreateEditPlayerState,
        imageUrl: String?,
        shotsLoggedRealtimeResponseList: List<ShotLoggedRealtimeResponse>
    ): Flow<Boolean> {
        val activeUser = activeUserRepository.fetchActiveUser()
        val key = activeUser?.firebaseAccountInfoKey ?: ""
        val playerKey = player.firebaseKey

        if (key.isEmpty() || playerKey.isEmpty()) {
            throw IllegalStateException("Active user key or player key is empty")
        }

        return updateFirebaseUserInfo.updatePlayer(
            playerInfoRealtimeWithKeyResponse = PlayerInfoRealtimeWithKeyResponse(
                playerFirebaseKey = playerKey,
                playerInfo = PlayerInfoRealtimeResponse(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    positionValue = state.playerPositionString.toPlayerPosition(application = application).value,
                    imageUrl = imageUrl ?: "",
                    shotsLogged = shotsLoggedRealtimeResponseList
                )
            )
        )
    }

    /**
     * Updates an existing player in the local Room database.
     *
     * This function updates the player data locally after successful Firebase update.
     * It uses the existing player as the reference for the update operation.
     *
     * Use cases:
     * - After successful Firebase update
     * - When saving player data locally for offline access
     * - As the final step in the edit player workflow
     *
     * @param currentPlayer The existing player object from the database.
     * @param newPlayer The updated player object with new information.
     */
    suspend fun updatePlayerInRoom(currentPlayer: Player, newPlayer: Player) {
        playerRepository.updatePlayer(
            currentPlayer = currentPlayer,
            newPlayer = newPlayer
        )
    }

    /**
     * Updates the UI state for an existing player loaded from the repository.
     *
     * This function populates the UI state with the existing player's information,
     * including name, image URL, position, and shots logged.
     *
     * Use cases:
     * - When loading an existing player for editing
     * - When initializing the edit screen with player data
     * - When refreshing player data after a shot deletion
     *
     * @param player The existing player to load into the state.
     * @param currentHintText The current hint text to use as a base.
     * @param sharedHelper The shared helper for generating hint text.
     * @return Updated CreateEditPlayerState with player information.
     */
    fun buildStateForExistingPlayer(
        player: Player,
        currentHintText: String,
        sharedHelper: CreateEditPlayerSharedHelper
    ): CreateEditPlayerState {
        return CreateEditPlayerState(
            firstName = player.firstName,
            lastName = player.lastName,
            editedPlayerUrl = player.imageUrl ?: "",
            toolbarNameResId = StringsIds.editPlayer,
            playerPositionString = application.getString(player.position.toType()),
            hintLogNewShotText = sharedHelper.hintLogNewShotText(
                firstName = player.firstName,
                lastName = player.lastName,
                currentHintText = currentHintText
            ),
            shots = player.shotsLoggedList
        )
    }

    /**
     * Processes the deleted shot flow and updates the player state if needed.
     *
     * This function handles the case when a shot is deleted from Firebase.
     * It refreshes the player data from the local database and updates the state.
     *
     * Use cases:
     * - When a shot is deleted and the player data needs to be refreshed
     * - When syncing deleted shots from Firebase
     * - As part of the shot deletion workflow
     *
     * @param hasDeletedShot Whether a shot has been deleted.
     * @param playerFirstName The first name of the player being edited.
     * @param playerLastName The last name of the player being edited.
     * @return Updated Player if found and refreshed, null otherwise.
     */
    suspend fun processHasDeletedShot(
        hasDeletedShot: Boolean,
        playerFirstName: String,
        playerLastName: String
    ): Player? {
        if (!hasDeletedShot) {
            return null
        }

        val player = playerRepository.fetchPlayerByName(
            firstName = playerFirstName,
            lastName = playerLastName
        ) ?: return null

        deleteFirebaseUserInfo.updateHasDeletedShotFlow(hasDeletedShot = false)
        return player
    }

    /**
     * Builds a Player object from the current state for local storage update.
     *
     * This function constructs a Player object with all the necessary information
     * from the UI state, using the existing player's Firebase key.
     *
     * Use cases:
     * - When preparing player data for local storage update
     * - After successful Firebase update
     * - When converting UI state to database entity for update
     *
     * @param existingPlayer The existing player being edited (for Firebase key).
     * @param state The current screen state containing player information.
     * @param imageUrl The Firebase Storage URL for the player's image, or empty string if no image.
     * @param shotsLoggedList List of shots logged for this player.
     * @return A Player object ready for local storage update.
     */
    fun buildPlayerFromState(
        existingPlayer: Player,
        state: CreateEditPlayerState,
        imageUrl: String,
        shotsLoggedList: List<com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged>
    ): Player {
        val positionString = state.playerPositionString.ifEmpty {
            application.getString(StringsIds.pointGuard)
        }
        return Player(
            firstName = state.firstName,
            lastName = state.lastName,
            position = positionString.toPlayerPosition(application = application),
            firebaseKey = existingPlayer.firebaseKey,
            imageUrl = imageUrl,
            shotsLoggedList = shotsLoggedList
        )
    }

    /**
     * Creates an alert dialog informing the user that no changes have been made to the current player.
     *
     * Use cases:
     * - When user tries to save without making any changes
     * - When validation detects no modifications
     *
     * @return Alert indicating no changes were detected.
     */
    fun noChangesHaveBeenMadeAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noChangesMade),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = application.getString(StringsIds.currentPlayerHasNoChangesDescription)
        )
    }
}

