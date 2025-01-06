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
import timber.log.Timber

class CreateFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDatabase: FirebaseDatabase
) : CreateFirebaseUserInfo {

    override fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse> {
        return callbackFlow {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(
                            element = CreateAccountFirebaseAuthResponse(
                                isSuccessful = true,
                                username = task.result?.additionalUserInfo?.username,
                                isNewUser = task.result?.additionalUserInfo?.isNewUser,
                                exception = null
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(attemptToCreateAccountFirebaseAuthResponseFlow) -> Creating account failed to create in Firebase Authentication, with following stack trace ${exception.stackTrace}")
                    trySend(
                        element = CreateAccountFirebaseAuthResponse(
                            isSuccessful = false,
                            username = null,
                            isNewUser = null,
                            exception = exception
                        )
                    )
                }
            awaitClose()
        }
    }

    override fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val createAccountResult = CreateAccountFirebaseRealtimeDatabaseResult(username = userName, email = email)
            val values = hashMapOf<String, String>()

            val uid = firebaseAuth.currentUser?.uid ?: ""
            val reference = firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid")

            values[Constants.USERNAME] = createAccountResult.username
            values[Constants.EMAIL] = createAccountResult.email

            reference.setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Pair(first = true, second = reference.key))
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow) -> Creating account failed to create in Firebase Realtime Database following stack trace ${exception.stackTrace}")
                    trySend(Pair(first = false, second = null))
                }
            awaitClose()
        }
    }

    override fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(playerInfoRealtimeResponse: PlayerInfoRealtimeResponse): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val reference = firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}")

            val values = hashMapOf<String, Any>()

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl
            values[Constants.SHOTS_LOGGED] = playerInfoRealtimeResponse.shotsLogged

            reference.push().setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = Pair(true, reference.key))
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow) -> Creating player failed to create in Firebase Realtime Database with following stack trace - ${exception.stackTrace}")
                    trySend(element = Pair(false, null))
                }
            awaitClose()
        }
    }

    override fun attemptToCreateImageFirebaseStorageResponseFlow(uri: Uri): Flow<String?> {
        return callbackFlow {
            val storageReference = firebaseStorage.getReference("${Constants.IMAGES}/${System.currentTimeMillis()}")

            storageReference.putFile(uri)
                .continueWithTask { storageReference.downloadUrl }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = task.result.toString())
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(attemptToCreateImageFirebaseStorageResponseFlow) -> Creating image url for player failed to create in Firebase Storage with following stack trace - ${exception.stackTrace}")
                    trySend(element = null)
                }

            awaitClose()
        }
    }

    override fun attemptToCreatePdfFirebaseStorageResponseFlow(uri: Uri): Flow<String?> {
        return callbackFlow {
            val storageReference = firebaseStorage.getReference("${Constants.PDFS}/${System.currentTimeMillis()}")

            storageReference.putFile(uri)
                .continueWithTask { storageReference.downloadUrl }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = task.result.toString())
                    } else {
                        Timber.w(message = "Warning(attemptToCreatePdfFirebaseStorageResponseFlow) -> Creating pdf url for player failed to successfully upload to server")
                        trySend(element = null)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(message = "Error(attemptToCreatePdfFirebaseStorageResponseFlow) -> Creating pdf url for player failed to create in Firebase Storage with following stack trace - ${exception.stackTrace}")
                    trySend(element = null)
                }

            awaitClose()
        }
    }
}
