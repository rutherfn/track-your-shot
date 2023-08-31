package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.data.firebase.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.Date

class ReadFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : ReadFirebaseUserInfo {

    override fun getLoggedInAccountEmail(): Flow<String?> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { user ->
                trySend(element = user.email)
            } ?: run {
                Timber.e(message = "Error(getLoggedInAccountEmail) -> Current firebase user is set to null")
                trySend(element = null)
            }
            awaitClose()
        }
    }

    override fun getAccountInfoFlowByEmail(email: String): Flow<AccountInfoRealtimeResponse?> {
        return callbackFlow {
            var accountInfoRealTimeResponse: AccountInfoRealtimeResponse? = null

            firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.ACCOUNT_INFO)
                .orderByChild(Constants.EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.children.count() == 1) {
                                snapshot.children.map { child ->
                                    accountInfoRealTimeResponse = child.getValue(
                                        AccountInfoRealtimeResponse::class.java
                                    )
                                }
                                trySend(element = accountInfoRealTimeResponse)
                            } else {
                                Timber.w(message = "Error(getAccountInfoFlowByEmail) -> Current snapshot contains the same email more then once")
                                trySend(element = null)
                            }
                        } else {
                            Timber.e(message = "Error(getAccountInfoFlowByEmail) -> Current snapshot does not exist")
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getAccountInfoFlowByEmail) -> Database error when attempting to get account info")
                        trySend(element = null)
                    }
                })
            awaitClose()
        }
    }

    override fun getAccountInfoKeyFlowByEmail(email: String): Flow<String?> {
        return callbackFlow {
            firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.ACCOUNT_INFO)
                .orderByChild(Constants.EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.firstOrNull()?.key?.let { childKey ->
                                trySend(element = childKey)
                            } ?: run {
                                Timber.w(message = "Warning(getAccountInfoKeyByEmail) -> Current snapshot exists but key does not exist")
                                trySend(element = null)
                            }
                        } else {
                            Timber.w(message = "Warning(getAccountInfoKeyByEmail) -> Current snapshot does not exist")
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Timber.e(message = "Error(getAccountInfoKeyByEmail) -> Database error when attempting to get account info key by email")
                        trySend(element = null)
                    }
                })
            awaitClose()
        }
    }

    override fun getPlayerInfoList(accountKey: String): Flow<List<PlayerInfoRealtimeResponse>> {
        return callbackFlow {
            val playerInfoRealtimeResponseArrayList: ArrayList<PlayerInfoRealtimeResponse> = arrayListOf()

            firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.ACCOUNT_INFO)
                .child(accountKey)
                .child(Constants.PLAYERS)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == 0L) {
                                trySend(element = emptyList())
                            } else {
                                for (playerSnapshot in snapshot.children) {
                                    playerSnapshot.getValue(PlayerInfoRealtimeResponse::class.java)
                                        ?.let {
                                            playerInfoRealtimeResponseArrayList.add(it)
                                        }
                                }
                            }

                            trySend(element = playerInfoRealtimeResponseArrayList.toList())
                        } else {
                            trySend(element = emptyList())
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        trySend(element = emptyList())
                    }
                })
            awaitClose()
        }
    }

    override fun getAccountInfoListFlow(): Flow<List<AccountInfoRealtimeResponse>?> {
        return callbackFlow {
            val accountInfoRealTimeResponseArrayList: ArrayList<AccountInfoRealtimeResponse> = arrayListOf()

            firebaseDatabase.getReference(Constants.USERS)
                .child(Constants.ACCOUNT_INFO)
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
                            Timber.e(message = "Error(getAccountInfoListFlow) -> Current snapshot does not exist")
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getAccountInfoListFlow) -> Database error when attempting to get account info")
                        trySend(element = null)
                    }
                })
            awaitClose()
        }
    }

    override fun getLastUpdatedDateFlow(): Flow<Date?> {
        return callbackFlow {
            firebaseDatabase.getReference(Constants.CONTENT_LAST_UPDATED_PATH)
                .child(Constants.LAST_UPDATED)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val lastUpdatedValue = snapshot.getValue(Long::class.java)

                            if (lastUpdatedValue != null) {
                                val lastUpdatedDate = Date(lastUpdatedValue)
                                trySend(lastUpdatedDate)
                            } else {
                                Timber.e(message = "Error(getLastUpdatedDateFlow) -> Value is null")
                                trySend(element = null)
                            }
                        } else {
                            Timber.e(message = "Error(getLastUpdatedDateFlow) -> Current snapshot does not exist")
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getLastUpdatedDateFlow) -> Database error when attempting to get updated date info")
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
                        Timber.e(message = "Error(isEmailVerifiedFlow) -> Add on complete listener was not successful")
                        trySend(element = false)
                    }
                }
            } ?: run {
                Timber.e(message = "Error(isEmailVerifiedFlow) -> Current user is set to null")
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
