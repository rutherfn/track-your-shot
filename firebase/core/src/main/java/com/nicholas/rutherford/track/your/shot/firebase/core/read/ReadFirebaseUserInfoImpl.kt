package com.nicholas.rutherford.track.your.shot.firebase.core.read

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
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
                                Timber.e(message = "Error(getLastUpdatedDateFlow) -> Value we are reading from is currently null")
                                trySend(element = null)
                            }
                        } else {
                            Timber.e(message = "Error(getLastUpdatedDateFlow) -> Current snapshot does not exist")
                            trySend(element = null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getLastUpdatedDateFlow) -> Database error when attempting to get updated date info ${error.message}")
                        trySend(element = null)
                    }
                })
            awaitClose()
        }
    }

    override fun getAccountInfoFlow(): Flow<AccountInfoRealtimeResponse?> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""

            firebaseDatabase.getReference("${Constants.USERS}/$uid")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val email = snapshot.child(Constants.EMAIL).getValue(String::class.java)
                            val username = snapshot.child(Constants.USERNAME).getValue(String::class.java)
                            safeLet(email, username) { accountEmail, accountUsername ->
                                trySend(element = AccountInfoRealtimeResponse(userName = accountUsername, email = accountEmail))
                            } ?: run {
                                Timber.e(message = "Error(getAccountInfoFlowByEmail) -> Current snapshot exists but email is $email and username is $username")
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

    override fun getDeletedShotIdsFlow(): Flow<List<Int>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""

            firebaseDatabase.getReference("${Constants.USERS}/$uid/${Constants.SHOT_IDS_TO_IGNORE}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val defaultShotIdsToIgnore = snapshot.getValue(object : GenericTypeIndicator<List<Int>>() {}) ?: emptyList()
                            trySend(element = defaultShotIdsToIgnore)
                        } else {
                            Timber.e(message = "Error(getDeletedShotIdsFlow) -> Current snapshot does not exist")
                            trySend(element = emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getDeletedShotIdsFlow) -> Database error when attempting to ge deleted shot ids from json with following stack trace - ${error.message}")
                        trySend(element = emptyList())
                    }
                })
            awaitClose()
        }
    }

    override fun getCreatedDeclaredShotsFlow(): Flow<List<DeclaredShotWithKeyRealtimeResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}"

            val declaredShotWithKeyRealtimeResponseArrayList: ArrayList<DeclaredShotWithKeyRealtimeResponse> = arrayListOf()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getCreatedDeclaredShotsFlow) -> No created shots currently exist for this account")
                                trySend(element = emptyList())
                            } else {
                                for (createdDeclaredShotSnapshot in snapshot.children) {
                                    val key = createdDeclaredShotSnapshot.key
                                    val info = createdDeclaredShotSnapshot.getValue(DeclaredShotRealtimeResponse::class.java)

                                    safeLet(key, info) { declaredShotFirebaseKey, declaredShotRealtimeResponse ->
                                        declaredShotWithKeyRealtimeResponseArrayList.add(
                                            DeclaredShotWithKeyRealtimeResponse(
                                                declaredShotFirebaseKey = declaredShotFirebaseKey,
                                                declaredShotRealtimeResponse = declaredShotRealtimeResponse
                                            )
                                        )
                                    }
                                }
                            }

                            trySend(declaredShotWithKeyRealtimeResponseArrayList.toList())
                        } else {
                            Timber.w("Warning(getCreatedDeclaredShotsFlow) -> No created shots reference currently exist for this account")
                            trySend(element = emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getCreatedDeclaredShotsFlow) -> Database error when attempting to get user created declared shot list with the following error ${error.message}")
                        trySend(element = emptyList())
                    }
                })

            awaitClose()
        }
    }

    override fun getAccountInfoKeyFlow(): Flow<String?> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            firebaseDatabase.getReference("${Constants.USERS}/$uid")
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

    override fun getPlayerInfoList(): Flow<List<PlayerInfoRealtimeWithKeyResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}"

            val playerInfoRealtimeWithKeyResponseArrayList: ArrayList<PlayerInfoRealtimeWithKeyResponse> = arrayListOf()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getPlayerInfoList) -> No players currently exist for this account")
                                trySend(element = emptyList())
                            } else {
                                for (playerSnapshot in snapshot.children) {
                                    val key = playerSnapshot.key
                                    val info = playerSnapshot.getValue(PlayerInfoRealtimeResponse::class.java)

                                    safeLet(key, info) { playerFirebaseKey, playerInfo ->
                                        playerInfoRealtimeWithKeyResponseArrayList.add(
                                            PlayerInfoRealtimeWithKeyResponse(playerFirebaseKey = playerFirebaseKey, playerInfo = playerInfo)
                                        )
                                    }
                                }
                            }

                            trySend(element = playerInfoRealtimeWithKeyResponseArrayList.toList())
                        } else {
                            trySend(element = emptyList())
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Timber.e(message = "Error(getPlayerInfoList) -> Database error when attempting to get user player list")
                        trySend(element = emptyList())
                    }
                })
            awaitClose()
        }
    }

    override fun getReportList(): Flow<List<IndividualPlayerReportWithKeyRealtimeResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}"

            val individualPlayerReportRealtimeWithKeyResponseArrayList: ArrayList<IndividualPlayerReportWithKeyRealtimeResponse> =
                arrayListOf()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getPlayerReportList) -> No player reports currently exist for this account")
                                trySend(element = emptyList())
                            } else {
                                for (reportSnapshot in snapshot.children) {
                                    val key = reportSnapshot.key
                                    val info = reportSnapshot.getValue(
                                        IndividualPlayerReportRealtimeResponse::class.java
                                    )

                                    safeLet(key, info) { reportFirebaseKey, playerReport ->
                                        individualPlayerReportRealtimeWithKeyResponseArrayList.add(
                                            IndividualPlayerReportWithKeyRealtimeResponse(
                                                reportFirebaseKey = reportFirebaseKey,
                                                playerReport = playerReport
                                            )
                                        )
                                    }
                                }
                            }

                            trySend(element = individualPlayerReportRealtimeWithKeyResponseArrayList)
                        } else {
                            Timber.w("Warning(getPlayerReportList) -> No player reports directory exist for this account")
                            trySend(element = emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e(message = "Error(getPlayerReportList) -> Database error when attempting to get user player report list with the following stack trace ${error.message}")
                        trySend(element = emptyList())
                    }
                })

            awaitClose()
        }
    }

    override fun isEmailVerifiedFlow(): Flow<Boolean> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { firebaseUser ->
                firebaseUser.reload()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            trySend(element = firebaseUser.isEmailVerified)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Timber.e(message = "Error(isEmailVerifiedFlow) -> We were not able to get info on if the email has been verified. Returned stack trace ${exception.stackTrace}")
                        trySend(element = false)
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
