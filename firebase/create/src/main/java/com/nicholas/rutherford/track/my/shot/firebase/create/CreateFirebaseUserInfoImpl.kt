package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.account.info.realtime.CreateAccountFirebaseRealtimeDatabaseResult
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

    override fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val createAccountResult = CreateAccountFirebaseRealtimeDatabaseResult(username = userName, email = email)
            val values = hashMapOf<String, String>()

            values[Constants.USERNAME] = createAccountResult.username
            values[Constants.EMAIL] = createAccountResult.email

            val pushReference = userReference.child(Constants.ACCOUNT_INFO).push()

            pushReference.setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentDate = Date()
                        launch { createFirebaseLastUpdated.attemptToCreateLastUpdatedFlow(date = currentDate).collect() }
                        trySend(Pair(task.isSuccessful, pushReference.key))
                    } else {
                        trySend(Pair(task.isSuccessful, null))
                    }
                }
            awaitClose()
        }
    }
}
