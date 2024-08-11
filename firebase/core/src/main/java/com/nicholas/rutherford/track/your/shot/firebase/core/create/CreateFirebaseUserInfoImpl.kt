package com.nicholas.rutherford.track.your.shot.firebase.core.create

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date

class CreateFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val createFirebaseLastUpdated: CreateFirebaseLastUpdated,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDatabase: FirebaseDatabase
) : CreateFirebaseUserInfo {

    override fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse> {
        return callbackFlow {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(
                            CreateAccountFirebaseAuthResponse(
                                isSuccessful = true,
                                username = task.result?.additionalUserInfo?.username,
                                isNewUser = task.result?.additionalUserInfo?.isNewUser,
                                exception = null
                            )
                        )
                    } else {
                        Timber.e(message = "Warning(attemptToCreateAccountFirebaseAuthResponseFlow) -> Creating account failed to create in Firebase Authentication")
                        trySend(
                            CreateAccountFirebaseAuthResponse(
                                isSuccessful = false,
                                username = null,
                                isNewUser = null,
                                exception = task.exception
                            )
                        )
                    }
                }
            awaitClose()
        }
    }

    override fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val createAccountResult = CreateAccountFirebaseRealtimeDatabaseResult(username = userName, email = email)
            val values = hashMapOf<String, String>()

            val uid = firebaseAuth.currentUser?.uid ?: ""

            values[Constants.USERNAME] = createAccountResult.username
            values[Constants.EMAIL] = createAccountResult.email

            firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid").push().setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentDate = Date()
                        launch { createFirebaseLastUpdated.attemptToCreateLastUpdatedFlow(date = currentDate).collect() }
                        trySend(Pair(first = task.isSuccessful, second = task.))
                    } else {
                        Timber.e(message = "Warning(attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow) -> Creating account failed to create in Firebase Realtime Database")
                        trySend(Pair(first = false, second = null))
                    }
                }
            awaitClose()
        }
    }

    override fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
        key: String,
        playerInfoRealtimeResponse: PlayerInfoRealtimeResponse
    ): Flow<Boolean> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val values = hashMapOf<String, Any>()

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl
            values[Constants.SHOTS_LOGGED] = playerInfoRealtimeResponse.shotsLogged

            firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}").push().setValue(values)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }

    override fun attemptToCreateImageFirebaseStorageResponseFlow(uri: Uri): Flow<String?> {
        return callbackFlow {
            val storageReference = firebaseStorage.getReference(Constants.IMAGES)
                .child(System.currentTimeMillis().toString())

            storageReference.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    storageReference.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        trySend(downloadUrl)
                    } else {
                        trySend(null)
                    }
                }
                .addOnFailureListener {
                    trySend(null)
                }

            awaitClose()
        }
    }
}
