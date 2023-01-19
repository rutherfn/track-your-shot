package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountFirebaseRealtimeDatabaseResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val USERS_PATH = "users"
const val USERNAME = "userName"
const val EMAIL = "email"
const val USER_INFO = "userInfo"

class CreateFirebaseUserInfoImpl(private val firebaseAuth: FirebaseAuth, firebaseDatabase: FirebaseDatabase) : CreateFirebaseUserInfo {

    private val userReference = firebaseDatabase.getReference(USERS_PATH)

    override fun attemptToCreateAccountFirebaseAuthResponseFlow(email: String, password: String): Flow<CreateAccountFirebaseAuthResponse> {
        return callbackFlow {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    trySend(
                        CreateAccountFirebaseAuthResponse(
                            isSuccessful = task.isSuccessful,
                            username = task.result?.additionalUserInfo?.username,
                            isNewUser = task.result?.additionalUserInfo?.isNewUser,
                            exception = task.exception
                        )
                    )
                }
            awaitClose()
        }
    }

    override fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName: String, email: String): Flow<Boolean> {
        return callbackFlow {
            val createAccountResult = CreateAccountFirebaseRealtimeDatabaseResult(username = userName, email = email)
            val values = hashMapOf<String, String>()

            values[USERNAME] = createAccountResult.username
            values[EMAIL] = createAccountResult.email

            userReference.child(USER_INFO).push().setValue(values)
                .addOnCompleteListener { task ->
                    trySend(task.isSuccessful)
                }
            awaitClose()
        }
    }
}
