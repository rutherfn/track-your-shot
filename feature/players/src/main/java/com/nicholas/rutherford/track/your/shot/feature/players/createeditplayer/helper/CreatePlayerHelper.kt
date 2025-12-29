package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper

import android.app.Application
import android.net.Uri
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.Center.toPlayerPosition
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerState
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow

/**
 * Helper class containing logic specific to creating new players.
 *
 * This class provides functions that are only used when creating a new player, such as:
 * - Checking if a player with the same name already exists
 * - Creating a player in Firebase
 * - Creating a player in the local Room database
 * - Handling the image upload for new players
 *
 * All functions are documented with their use cases and expected behavior.
 */
class CreatePlayerHelper(
    private val application: Application,
    private val playerRepository: PlayerRepository,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo
) {

    /**
     * Checks if a player with the same name already exists before creating.
     *
     * This function queries the local database to see if a player with the given
     * first and last name already exists. This prevents duplicate player creation.
     *
     * Use cases:
     * - Before creating a new player to prevent duplicates
     * - When user submits the create player form
     * - As part of the validation flow before Firebase creation
     *
     * @param state The current screen state containing player information.
     * @return The existing player if found, null if no player exists with that name.
     */
    suspend fun checkIfPlayerAlreadyExists(state: CreateEditPlayerState): Player? {
        return playerRepository.fetchPlayerByName(
            firstName = state.firstName,
            lastName = state.lastName
        )
    }

    /**
     * Creates a new player in Firebase Realtime Database.
     *
     * This function creates a player entry in Firebase with all the player information
     * including name, position, image URL, and shots logged. It returns a Flow that
     * emits the result of the creation operation.
     *
     * Use cases:
     * - When creating a new player after validation passes
     * - When syncing a new player to Firebase
     * - As part of the create player workflow
     *
     * @param state The current screen state containing player information.
     * @param imageUrl The Firebase Storage URL for the player's image, or null if no image.
     * @param shotsLoggedRealtimeResponseList List of shots in Firebase realtime response format.
     * @return Flow that emits a Pair of (isSuccessful: Boolean, firebaseKey: String?).
     *         The firebaseKey is the unique identifier assigned by Firebase.
     */
    fun createUserInFirebase(
        state: CreateEditPlayerState,
        imageUrl: String?,
        shotsLoggedRealtimeResponseList: List<com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse>
    ): Flow<Pair<Boolean, String?>> {
        return createFirebaseUserInfo.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
            playerInfoRealtimeResponse = PlayerInfoRealtimeResponse(
                firstName = state.firstName,
                lastName = state.lastName,
                positionValue = state.playerPositionString.toPlayerPosition(application = application).value,
                imageUrl = imageUrl ?: "",
                shotsLogged = shotsLoggedRealtimeResponseList
            )
        )
    }

    /**
     * Creates a new player in the local Room database.
     *
     * This function saves the player data locally after successful Firebase creation.
     * The player is stored with the Firebase key for future synchronization.
     *
     * Use cases:
     * - After successful Firebase creation
     * - When saving player data locally for offline access
     * - As the final step in the create player workflow
     *
     * @param player The player object to save in the local database.
     */
    suspend fun createPlayerInRoom(player: Player) {
        playerRepository.createPlayer(player = player)
    }

    /**
     * Handles the image upload for a new player.
     *
     * This function attempts to upload the selected image to Firebase Storage.
     * It returns a Flow that emits the image URL if successful, or null if the upload fails.
     *
     * Use cases:
     * - When user selects an image for a new player
     * - As part of the create player workflow before creating the player in Firebase
     * - When processing image uploads for new players
     *
     * @param uri The URI of the image to upload.
     * @return Flow that emits the image URL string if successful, null if upload fails.
     */
    fun uploadImageToFirebase(uri: Uri): Flow<String?> {
        return createFirebaseUserInfo.attemptToCreateImageFirebaseStorageResponseFlow(uri = uri)
    }

    /**
     * Builds a Player object from the current state for local storage.
     *
     * This function constructs a Player object with all the necessary information
     * from the UI state, including default values where needed.
     *
     * Use cases:
     * - When preparing player data for local storage
     * - After successful Firebase creation
     * - When converting UI state to database entity
     *
     * @param state The current screen state containing player information.
     * @param firebaseKey The Firebase key assigned to this player.
     * @param imageUrl The Firebase Storage URL for the player's image, or empty string if no image.
     * @param shotsLoggedList List of shots logged for this player.
     * @return A Player object ready for local storage.
     */
    fun buildPlayerFromState(
        state: CreateEditPlayerState,
        firebaseKey: String,
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
            firebaseKey = firebaseKey,
            imageUrl = imageUrl,
            shotsLoggedList = shotsLoggedList
        )
    }
}

