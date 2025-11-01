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
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.SavedVoiceCommandRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber
import java.util.Date

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [ReadFirebaseUserInfo] for reading Firebase user data.
 * Provides reactive flows to access user account info, created shots, player info,
 * individual player reports, deleted shot IDs, and login/email verification status.
 */
class ReadFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : ReadFirebaseUserInfo {

    /**
     * Retrieves the email of the currently logged-in Firebase user.
     *
     * @return [Flow] emitting the user's email if logged in, or null otherwise.
     */
    override fun getLoggedInAccountEmail(): Flow<String?> {
        return callbackFlow {
            firebaseAuth.currentUser?.let { user ->
                trySend(element = user.email)
            } ?: run {
                Timber.e("Error(getLoggedInAccountEmail) -> Current firebase user is null")
                trySend(element = null)
            }
            awaitClose()
        }
    }

    /**
     * Retrieves the last updated timestamp from Firebase.
     *
     * @return [Flow] emitting the last update [Date], or null if unavailable.
     */
    override fun getLastUpdatedDateFlow(): Flow<Date?> {
        return callbackFlow {
            firebaseDatabase.getReference(Constants.CONTENT_LAST_UPDATED_PATH)
                .child(Constants.LAST_UPDATED)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val lastUpdatedValue = snapshot.getValue(Long::class.java)
                            if (lastUpdatedValue != null) {
                                trySend(Date(lastUpdatedValue))
                            } else {
                                Timber.e("Error(getLastUpdatedDateFlow) -> Value is null")
                                trySend(null)
                            }
                        } else {
                            Timber.e("Error(getLastUpdatedDateFlow) -> Snapshot does not exist")
                            trySend(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("Error(getLastUpdatedDateFlow) -> Database error: ${error.message}")
                        trySend(null)
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves account information (username and email) for the current user.
     *
     * @return [Flow] emitting [AccountInfoRealtimeResponse] or null if data is missing.
     */
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
                                trySend(AccountInfoRealtimeResponse(userName = accountUsername, email = accountEmail))
                            } ?: run {
                                Timber.e("Error(getAccountInfoFlow) -> Email or username is null")
                                trySend(null)
                            }
                        } else {
                            Timber.e("Error(getAccountInfoFlow) -> Snapshot does not exist")
                            trySend(null)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("Error(getAccountInfoFlow) -> Database error")
                        trySend(null)
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves a list of shot IDs marked as deleted for the current user.
     *
     * @return [Flow] emitting a list of deleted shot IDs, or empty list if none exist.
     */
    override fun getDeletedShotIdsFlow(): Flow<List<Int>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            firebaseDatabase.getReference("${Constants.USERS}/$uid/${Constants.SHOT_IDS_TO_IGNORE}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val list = snapshot.getValue(object : GenericTypeIndicator<List<Int>>() {}) ?: emptyList()
                            trySend(list)
                        } else {
                            Timber.e("Error(getDeletedShotIdsFlow) -> Snapshot does not exist")
                            trySend(emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("Error(getDeletedShotIdsFlow) -> Database error: ${error.message}")
                        trySend(emptyList())
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves all declared shots created by the user, along with their Firebase keys.
     *
     * @return [Flow] emitting a list of [DeclaredShotWithKeyRealtimeResponse], empty if none exist.
     */
    override fun getCreatedDeclaredShotsFlow(): Flow<List<DeclaredShotWithKeyRealtimeResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}"
            val declaredShotList = arrayListOf<DeclaredShotWithKeyRealtimeResponse>()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getCreatedDeclaredShotsFlow) -> No shots exist")
                                trySend(emptyList())
                            } else {
                                for (child in snapshot.children) {
                                    val key = child.key
                                    val info = child.getValue(DeclaredShotRealtimeResponse::class.java)
                                    safeLet(key, info) { k, i ->
                                        declaredShotList.add(DeclaredShotWithKeyRealtimeResponse(k, i))
                                    }
                                }
                                trySend(declaredShotList.toList())
                            }
                        } else {
                            Timber.w("Warning(getCreatedDeclaredShotsFlow) -> Snapshot does not exist")
                            trySend(emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("Error(getCreatedDeclaredShotsFlow) -> Database error: ${error.message}")
                        trySend(emptyList())
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves the Firebase key for the current user's account info.
     *
     * @return [Flow] emitting the account info key, or null if not found.
     */
    override fun getAccountInfoKeyFlow(): Flow<String?> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            firebaseDatabase.getReference("${Constants.USERS}/$uid")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.firstOrNull()?.key?.let { key ->
                            trySend(key)
                        } ?: run {
                            Timber.w("Warning(getAccountInfoKeyFlow) -> Key does not exist")
                            trySend(null)
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Timber.e("Error(getAccountInfoKeyFlow) -> Database error")
                        trySend(null)
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves a list of all players associated with the current user, including keys.
     *
     * @return [Flow] emitting a list of [PlayerInfoRealtimeWithKeyResponse], empty if none exist.
     */
    override fun getPlayerInfoList(): Flow<List<PlayerInfoRealtimeWithKeyResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}"
            val playerList = arrayListOf<PlayerInfoRealtimeWithKeyResponse>()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getPlayerInfoList) -> No players exist")
                                trySend(emptyList())
                            } else {
                                for (playerSnapshot in snapshot.children) {
                                    val key = playerSnapshot.key
                                    val info = playerSnapshot.getValue(PlayerInfoRealtimeResponse::class.java)
                                    safeLet(key, info) { k, i ->
                                        playerList.add(PlayerInfoRealtimeWithKeyResponse(k, i))
                                    }
                                }
                                trySend(playerList.toList())
                            }
                        } else {
                            trySend(emptyList())
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Timber.e("Error(getPlayerInfoList) -> Database error")
                        trySend(emptyList())
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves a list of all saved voice commands associated with the current user, including keys.
     *
     * @return [Flow] emitting a list of [SavedVoiceCommandRealtimeWithKeyResponse], empty if none exist.
     */
    override fun getSavedVoiceCommandList(): Flow<List<SavedVoiceCommandRealtimeWithKeyResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS_PATH}/$uid/${Constants.SAVED_VOICE_COMMANDS}"
            val savedVoiceCommandList = arrayListOf<SavedVoiceCommandRealtimeWithKeyResponse>()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getSavedVoiceCommandList) -> No saved voice commands exist")
                                trySend(emptyList())
                            }
                            for (savedVoiceCommandSnapshot in snapshot.children) {
                                val key = savedVoiceCommandSnapshot.key
                                val info = savedVoiceCommandSnapshot.getValue(SavedVoiceCommandRealtimeResponse::class.java)
                                safeLet(key, info) { currentKey, currentInfo ->
                                    savedVoiceCommandList.add(SavedVoiceCommandRealtimeWithKeyResponse(savedVoiceCommandKey = currentKey, savedVoiceCommandInfo = currentInfo))
                                }
                            }

                            trySend(savedVoiceCommandList.toList())
                        } else {
                            Timber.w("Warning(getSavedVoiceCommandList) -> Current firebase route does not exist for this user")
                            trySend(emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("Error(getSavedVoiceCommandList) -> Database error")
                        trySend(emptyList())
                    }
                })
            awaitClose()
        }
    }

    /**
     * Retrieves a list of individual player reports associated with the current user.
     *
     * @return [Flow] emitting a list of [IndividualPlayerReportWithKeyRealtimeResponse], empty if none exist.
     */
    override fun getReportList(): Flow<List<IndividualPlayerReportWithKeyRealtimeResponse>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}"
            val reportList = arrayListOf<IndividualPlayerReportWithKeyRealtimeResponse>()

            firebaseDatabase.getReference(path)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            if (snapshot.childrenCount == Constants.FIREBASE_CHILDREN_COUNT_ZERO) {
                                Timber.w("Warning(getReportList) -> No reports exist")
                                trySend(emptyList())
                            } else {
                                for (reportSnapshot in snapshot.children) {
                                    val key = reportSnapshot.key
                                    val info = reportSnapshot.getValue(IndividualPlayerReportRealtimeResponse::class.java)
                                    safeLet(key, info) { k, i ->
                                        reportList.add(IndividualPlayerReportWithKeyRealtimeResponse(k, i))
                                    }
                                }
                                trySend(reportList)
                            }
                        } else {
                            Timber.w("Warning(getReportList) -> Reports directory does not exist")
                            trySend(emptyList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Timber.e("Error(getReportList) -> Database error: ${error.message}")
                        trySend(emptyList())
                    }
                })
            awaitClose()
        }
    }

    /**
     * Checks whether the logged-in user's email is verified.
     *
     * @return [Flow] emitting true (stubbed, real Firebase check not implemented due to update made from Firebase on this).
     * May not even need it todo -> Come back to this in the future
     */
    override fun isEmailVerifiedFlow(): Flow<Boolean> {
        return flowOf(true)
    }

    /**
     * Checks whether there is a logged-in Firebase user.
     *
     * @return [Flow] emitting true if logged in, false otherwise.
     */
    override fun isLoggedInFlow(): Flow<Boolean> {
        return callbackFlow {
            trySend(firebaseAuth.currentUser != null)
            awaitClose()
        }
    }
}
