package com.nicholas.rutherford.track.your.shot.firebase.core.create

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.CreateAccountFirebaseRealtimeDatabaseResult
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [CreateFirebaseUserInfo] for creating users, players, reports, declared shots,
 * and media files (images, PDFs) in Firebase Authentication, Realtime Database, and Storage.
 *
 * @property firebaseAuth Firebase Authentication instance for managing accounts.
 * @property firebaseStorage Firebase Storage instance for uploading images and PDFs.
 * @property firebaseDatabase Firebase Realtime Database instance for structured data storage.
 */
class CreateFirebaseUserInfoImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDatabase: FirebaseDatabase
) : CreateFirebaseUserInfo {

    /**
     * Attempts to create a Firebase Authentication account using email and password.
     * Emits [CreateAccountFirebaseAuthResponse] indicating success or failure.
     *
     * @param email Email for the account.
     * @param password Password for the account.
     * @return [Flow] of [CreateAccountFirebaseAuthResponse]
     */
    override fun attemptToCreateAccountFirebaseAuthResponseFlow(
        email: String,
        password: String
    ): Flow<CreateAccountFirebaseAuthResponse> {
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
                    Timber.e(
                        message = "Error(attemptToCreateAccountFirebaseAuthResponseFlow) -> " +
                                "Creating account failed to create in Firebase Authentication, with following stack trace ${exception.message}"
                    )
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

    /**
     * Creates default shot IDs to ignore in Firebase Realtime Database.
     *
     * @param defaultShotIdsToIgnore List of shot IDs to ignore.
     * @return [Flow] emitting a [Pair] of success status and the stored list.
     */
    override fun attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(
        defaultShotIdsToIgnore: List<Int>
    ): Flow<Pair<Boolean, List<Int>?>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val reference =
                firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid/${Constants.SHOT_IDS_TO_IGNORE}")

            reference.setValue(defaultShotIdsToIgnore)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Pair(first = true, second = defaultShotIdsToIgnore))
                    } else {
                        Timber.w(
                            message = "Warning(attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow) -> Creating default shot ids to ignore has failed in Firebase Realtime Database."
                        )
                        trySend(Pair(first = false, second = null))
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(
                        message = "Error(attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow) -> Creating account failed to create in Firebase Realtime Database following stack trace ${exception.stackTrace}"
                    )
                    trySend(Pair(first = false, second = null))
                }
            awaitClose()
        }
    }

    /**
     * Adds a declared shot to Firebase Realtime Database.
     *
     * @param declaredShot The declared shot data.
     * @return [Flow] emitting a [Pair] of success status and the generated Firebase key.
     */
    override fun attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(
        declaredShot: DeclaredShot
    ): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val values = hashMapOf<String, Any>()
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val reference = firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}")
            val key = reference.push().key ?: ""

            values[Constants.ID] = declaredShot.id
            values[Constants.SHOT_CATEGORY] = declaredShot.shotCategory
            values[Constants.TITLE] = declaredShot.title
            values[Constants.DESCRIPTION] = declaredShot.description

            reference.child(key).setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Pair(first = true, second = key))
                    } else {
                        Timber.w(
                            message = "Warning(attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow) -> Creating declared shot failed to create in Firebase Realtime Database."
                        )
                        trySend(Pair(first = false, second = null))
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(
                        message = "Error(attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow) -> Creating declared shot failed to create in Firebase Realtime Database following stack trace ${exception.stackTrace}"
                    )
                    trySend(Pair(first = false, second = null))
                }

            awaitClose()
        }
    }

    /**
     * Creates a user record in Firebase Realtime Database.
     *
     * @param userName Username for the account.
     * @param email Email for the account.
     * @return [Flow] emitting a [Pair] of success status and the reference key.
     */
    override fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
        userName: String,
        email: String
    ): Flow<Pair<Boolean, String?>> {
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
                    Timber.e(
                        message = "Error(attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow) -> Creating account failed to create in Firebase Realtime Database following stack trace ${exception.stackTrace}"
                    )
                    trySend(Pair(first = false, second = null))
                }
            awaitClose()
        }
    }

    /**
     * Adds a player to Firebase Realtime Database.
     *
     * @param playerInfoRealtimeResponse Player information.
     * @return [Flow] emitting a [Pair] of success status and the Firebase key.
     */
    override fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
        playerInfoRealtimeResponse: PlayerInfoRealtimeResponse
    ): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val reference = firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}")
            val values = hashMapOf<String, Any>()
            val key = reference.push().key ?: ""

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl
            values[Constants.SHOTS_LOGGED] = playerInfoRealtimeResponse.shotsLogged

            reference.child(key).setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = Pair(true, key))
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(
                        message = "Error(attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow) -> Creating player failed to create in Firebase Realtime Database with following stack trace - ${exception.stackTrace}"
                    )
                    trySend(element = Pair(false, null))
                }
            awaitClose()
        }
    }

    /**
     * Adds an individual player report to Firebase Realtime Database.
     *
     * @param individualPlayerReportRealtimeResponse The report data.
     * @return [Flow] emitting a [Pair] of success status and the Firebase key.
     */
    override fun attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(
        individualPlayerReportRealtimeResponse: IndividualPlayerReportRealtimeResponse
    ): Flow<Pair<Boolean, String?>> {
        return callbackFlow {
            val uid = firebaseAuth.currentUser?.uid ?: ""
            val reference = firebaseDatabase.getReference("${Constants.USERS_PATH}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}")
            val values = hashMapOf<String, Any>()
            val key = reference.push().key ?: ""

            values[Constants.LOGGED_DATE_VALUE] = individualPlayerReportRealtimeResponse.loggedDateValue
            values[Constants.PLAYER_NAME] = individualPlayerReportRealtimeResponse.playerName
            values[Constants.PDF_URL] = individualPlayerReportRealtimeResponse.pdfUrl

            reference.child(key).setValue(values)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = Pair(true, key))
                    } else {
                        Timber.w(
                            message = "Warning(attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow) -> Creating player report failed to create in Firebase Realtime Database,"
                        )
                        trySend(element = Pair(false, null))
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(
                        message = "Error(attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow) -> Creating player report failed to create in Firebase Realtime Database with following stack trace - ${exception.stackTrace}"
                    )
                    trySend(element = Pair(false, null))
                }
            awaitClose()
        }
    }

    /**
     * Uploads an image to Firebase Storage and retrieves its URL.
     *
     * @param uri Image URI to upload.
     * @return [Flow] emitting the URL or null if failed.
     */
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
                    Timber.e(
                        message = "Error(attemptToCreateImageFirebaseStorageResponseFlow) -> Creating image url for player failed to create in Firebase Storage with following stack trace - ${exception.stackTrace}"
                    )
                    trySend(element = null)
                }

            awaitClose()
        }
    }

    /**
     * Uploads a PDF to Firebase Storage and retrieves its URL.
     *
     * @param uri PDF URI to upload.
     * @return [Flow] emitting the URL or null if failed.
     */
    override fun attemptToCreatePdfFirebaseStorageResponseFlow(uri: Uri): Flow<String?> {
        return callbackFlow {
            val storageReference = firebaseStorage.getReference("${Constants.PDFS}/${System.currentTimeMillis()}")

            storageReference.putFile(uri)
                .continueWithTask { storageReference.downloadUrl }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(element = task.result.toString())
                    } else {
                        Timber.w(
                            message = "Warning(attemptToCreatePdfFirebaseStorageResponseFlow) -> Creating pdf url for player failed to successfully upload to server"
                        )
                        trySend(element = null)
                    }
                }
                .addOnFailureListener { exception ->
                    Timber.e(
                        message = "Error(attemptToCreatePdfFirebaseStorageResponseFlow) -> Creating pdf url for player failed to create in Firebase Storage with following stack trace - ${exception.stackTrace}"
                    )
                    trySend(element = null)
                }

            awaitClose()
        }
    }
}

