package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class UpdateFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : UpdateFirebaseUserInfo {

    override fun updatePlayer(playerInfoRealtimeWithKeyResponse: PlayerInfoRealtimeWithKeyResponse): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/${playerInfoRealtimeWithKeyResponse.playerFirebaseKey}"

            val playerDataToUpdate =
                mapOf(
                    Constants.FIRST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
                    Constants.LAST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
                    Constants.IMAGE_URL to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
                    Constants.POSITION_VALUE to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue,
                    Constants.SHOTS_LOGGED to playerInfoRealtimeWithKeyResponse.playerInfo.shotsLogged
                )

            firebaseDatabase.getReference(path)
                .updateChildren(playerDataToUpdate)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = true)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.w(message = "Error(updatePlayer) -> Was not able to update current player from given account. With following stack trace ${exception.stackTrace}")
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
