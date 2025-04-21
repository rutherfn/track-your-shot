package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse
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
                    } else {
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.w(message = "Error(updatePlayer) -> Was not able to update current player from given account. With following stack trace ${exception.stackTrace}")
                    trySend(element = false)
                }
            awaitClose()
        }
    }

    override fun updateDeclaredShot(declaredShotWithKeyRealtimeResponse: DeclaredShotWithKeyRealtimeResponse): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}/${declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey}"

            val declaredShotDataToUpdate =
                mapOf(
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
                        Timber.w(message = "Warning(updateDeclaredShot) -> Was not able to update declared shot for given account.")
                        trySend(element = false)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(updateDeclaredShot) -> Was not able to update declared shot for given account. With the following stack trace ${exception.message}")
                    trySend(element = false)
                }
            awaitClose()
        }
    }
}
