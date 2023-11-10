package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class UpdateFirebaseUserInfoImpl(private val firebaseDatabase: FirebaseDatabase) : UpdateFirebaseUserInfo {
    override fun updatePlayer(
        accountKey: String,
        playerInfoRealtimeWithKeyResponse: PlayerInfoRealtimeWithKeyResponse
    ): Flow<Boolean> {
        val playerDataToUpdate = mapOf(
            "firstName" to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
            "lastName" to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
            "imageUrl" to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
            "positionValue" to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue
        )
        return callbackFlow {
            firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.ACCOUNT_INFO)
                .child(accountKey)
                .child(Constants.PLAYERS)
                .child(playerInfoRealtimeWithKeyResponse.playerFirebaseKey)
                .updateChildren(playerDataToUpdate)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    } else {
                        Timber.w(message = "Warning(updatePlayer) -> Was not able to update current player from given account.")
                        trySend(element = false)
                    }
                }
            awaitClose()
        }
    }
}
