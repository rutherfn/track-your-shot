package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [UpdateFirebaseUserInfo] interface.
 * Provides reactive [Flow] methods for updating Firebase Realtime Database records
 * related to users, players, and declared shots.
 */
class UpdateFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : UpdateFirebaseUserInfo {

    /**
     * Updates a saved voice command information in Firebase Realtime Database.
     *
     * @param savedVoiceCommandRealtimeWithKeyResponse Contains the saved voice command data along with their Firebase key.
     * @return [Flow] emitting true if the update was successful, false otherwise.
     */
    override fun updateSavedVoiceCommand(savedVoiceCommandRealtimeWithKeyResponse: SavedVoiceCommandRealtimeWithKeyResponse): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS_PATH}/$uid/${Constants.SAVED_VOICE_COMMANDS}/${savedVoiceCommandRealtimeWithKeyResponse.savedVoiceCommandKey}"

            val savedVoiceCommandDataToUpdate = mapOf(
                Constants.NAME to savedVoiceCommandRealtimeWithKeyResponse.savedVoiceCommandInfo.name,
                Constants.TYPE_VALUE to savedVoiceCommandRealtimeWithKeyResponse.savedVoiceCommandInfo.typeValue
            )

            firebaseDatabase.getReference(path)
                .updateChildren(savedVoiceCommandDataToUpdate)
                .addOnCompleteListener { task ->
                    trySend(element = task.isSuccessful)
                }
                .addOnFailureListener { exception ->
                    Timber.w(
                        message = "Error(updateSavedVoiceCommand) -> Unable to update saved voice command for account. Stack trace: ${exception.stackTrace}"
                    )
                    trySend(element = false)
                }
            awaitClose()
        }
    }

    /**
     * Updates a player's information in Firebase Realtime Database.
     *
     * @param playerInfoRealtimeWithKeyResponse Contains the player's data along with their Firebase key.
     * @return [Flow] emitting true if the update was successful, false otherwise.
     */
    override fun updatePlayer(playerInfoRealtimeWithKeyResponse: PlayerInfoRealtimeWithKeyResponse): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/${playerInfoRealtimeWithKeyResponse.playerFirebaseKey}"

            val playerDataToUpdate = mapOf(
                Constants.FIRST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
                Constants.LAST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
                Constants.IMAGE_URL to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
                Constants.POSITION_VALUE to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue,
                Constants.SHOTS_LOGGED to playerInfoRealtimeWithKeyResponse.playerInfo.shotsLogged
            )

            firebaseDatabase.getReference(path)
                .updateChildren(playerDataToUpdate)
                .addOnCompleteListener { task ->
                    trySend(element = task.isSuccessful)
                }
                .addOnFailureListener { exception ->
                    Timber.w(
                        message = "Error(updatePlayer) -> Unable to update player for account. Stack trace: ${exception.stackTrace}"
                    )
                    trySend(element = false)
                }
            awaitClose()
        }
    }

    /**
     * Updates a declared shot's information in Firebase Realtime Database.
     *
     * @param declaredShotWithKeyRealtimeResponse Contains the declared shot's data along with its Firebase key.
     * @return [Flow] emitting true if the update was successful, false otherwise.
     */
    override fun updateDeclaredShot(declaredShotWithKeyRealtimeResponse: DeclaredShotWithKeyRealtimeResponse): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}/${declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey}"

            val declaredShotDataToUpdate = mapOf(
                Constants.ID to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                Constants.SHOT_CATEGORY to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                Constants.TITLE to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                Constants.DESCRIPTION to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description
            )

            firebaseDatabase.getReference(path)
                .updateChildren(declaredShotDataToUpdate)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w("Warning(updateDeclaredShot) -> Failed to update declared shot for this account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(
                        message = "Error(updateDeclaredShot) -> Failed to update declared shot. Stack trace: ${exception.message}"
                    )
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
