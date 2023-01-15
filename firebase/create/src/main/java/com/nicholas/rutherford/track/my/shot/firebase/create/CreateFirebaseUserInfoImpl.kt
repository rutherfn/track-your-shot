package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CreateFirebaseUserInfoImpl(private val firebaseAuth: FirebaseAuth) : CreateFirebaseUserInfo {
    override fun attemptToCreateAccountResponseFlow(email: String, password: String): Flow<CreateAccountResponse> {
        return callbackFlow {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    trySend(
                        CreateAccountResponse(
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
}
