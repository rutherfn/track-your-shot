package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.data.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.data.firebase.CreateAccountFirebaseRealtimeDatabaseResult
import com.nicholas.rutherford.track.my.shot.data.firebase.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

class CreateFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val createFirebaseLastUpdated: CreateFirebaseLastUpdated,
    firebaseDatabase: FirebaseDatabase
) : CreateFirebaseUserInfo {

    private val userReference = firebaseDatabase.getReference(Constants.USERS_PATH)

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

    override fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Boolean> {
        return callbackFlow {
            val createAccountResult =
                CreateAccountFirebaseRealtimeDatabaseResult(
                    username = userName,
                    email = email
                )
            val values = hashMapOf<String, String>()

            values[Constants.USERNAME] = createAccountResult.username
            values[Constants.EMAIL] = createAccountResult.email

            userReference.child(Constants.ACCOUNT_INFO).push().setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentDate = Date()
                        launch { createFirebaseLastUpdated.attemptToCreateLastUpdatedFlow(date = currentDate).collect() }
                    }
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }

    override fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
        key: String,
        playerInfoRealtimeResponse: PlayerInfoRealtimeResponse
    ): Flow<Boolean> {
        return callbackFlow {
            val userAccountInfoDatabaseReference =
                FirebaseDatabase.getInstance()
                    .getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(key)
                    .child(Constants.PLAYERS)

            val newPlayerReference = userAccountInfoDatabaseReference.push()
            val values = hashMapOf<String, Any>()

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl

            newPlayerReference.setValue(values)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
