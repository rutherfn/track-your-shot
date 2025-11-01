package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface for updating Firebase Realtime Database user information.
 * Provides reactive [Flow] methods for updating players and declared shots.
 */
interface UpdateFirebaseUserInfo {

    /**
     * Updates a player's information in Firebase Realtime Database.
     *
     * @param playerInfoRealtimeWithKeyResponse Contains the player's data along with their Firebase key.
     * @return [Flow] emitting true if the update was successful, false otherwise.
     */
    fun updatePlayer(playerInfoRealtimeWithKeyResponse: PlayerInfoRealtimeWithKeyResponse): Flow<Boolean>

    /**
     * Updates a saved voice command information in Firebase Realtime Database.
     *
     * @param savedVoiceCommandRealtimeWithKeyResponse Contains the saved voice command data along with their Firebase key.
     * @return [Flow] emitting true if the update was successful, false otherwise.
     */
    fun updateSavedVoiceCommand(savedVoiceCommandRealtimeWithKeyResponse: SavedVoiceCommandRealtimeWithKeyResponse): Flow<Boolean>

    /**
     * Updates a declared shot's information in Firebase Realtime Database.
     *
     * @param declaredShotWithKeyRealtimeResponse Contains the declared shot's data along with its Firebase key.
     * @return [Flow] emitting true if the update was successful, false otherwise.
     */
    fun updateDeclaredShot(declaredShotWithKeyRealtimeResponse: DeclaredShotWithKeyRealtimeResponse): Flow<Boolean>
}
