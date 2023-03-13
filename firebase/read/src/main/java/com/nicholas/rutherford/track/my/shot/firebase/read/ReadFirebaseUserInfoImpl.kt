package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nicholas.rutherford.track.my.shot.account.info.realtime.AccountInfoRealtimeResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val ACCOUNT_INFO = "accountInfo"
const val EMAIL = "email"
const val USERS = "users"

class ReadFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : ReadFirebaseUserInfo {

    override fun getLoggedInAccountEmail(): Flow<String?> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { user ->
                trySend(element = user.email)
            } ?: run {
                trySend(element = null)
            }
            awaitClose()
        }
    }

    override fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?> {
        return callbackFlow {
            var accountInfoRealTimeResponse: AccountInfoRealtimeResponse? = null

            firebaseDatabase.getReference(USERS)
                .child(ACCOUNT_INFO)
                .orderByChild(EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.children.count() == 1) {
                                snapshot.children.map { child ->
                                    accountInfoRealTimeResponse = child.getValue(AccountInfoRealtimeResponse::class.java)
                                }
                                trySend(element = accountInfoRealTimeResponse)
                            } else {
                                trySend(element = null)
                            }
                        } else {
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(element = null)
                    }
                })
            awaitClose()
        }
    }

    override fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?> {
        return callbackFlow {
            val accountInfoRealTimeResponseArrayList: ArrayList<AccountInfoRealtimeResponse> = arrayListOf()

            firebaseDatabase.getReference(USERS)
                .child(ACCOUNT_INFO)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.map { child ->
                                child.getValue(AccountInfoRealtimeResponse::class.java)
                                    ?.let { accountInfoRealTimeResponse ->
                                        accountInfoRealTimeResponseArrayList.add(accountInfoRealTimeResponse)
                                    }
                            }
                            trySend(element = accountInfoRealTimeResponseArrayList.toList())
                        } else {
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(element = null)
                    }
                })
            awaitClose()
        }
    }

    override fun isEmailVerifiedFlow(): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { firebaseUser ->
                firebaseUser.reload().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = firebaseUser.isEmailVerified)
                    } else {
                        trySend(element = false)
                    }
                }
            } ?: run {
                trySend(element = false)
            }
            awaitClose()
        }
    }

    override fun isLoggedInFlow(): Flow<Boolean> {
        return callbackFlow {
            trySend(element = firebaseAuth.currentUser != null)
            awaitClose()
        }
    }
}
