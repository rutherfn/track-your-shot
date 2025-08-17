package com.nicholas.rutherford.track.your.shot.firebase.core.create

import android.net.Uri
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.firebase.CreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import kotlinx.coroutines.flow.Flow

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface defining Firebase operations for creating users, players, reports, declared shots,
 * and uploading media files such as images and PDFs.
 */
interface CreateFirebaseUserInfo {

    /**
     * Attempts to create a new Firebase Authentication account using an email and password.
     *
     * @param email The email for the new account.
     * @param password The password for the new account.
     * @return [Flow] emitting a [CreateAccountFirebaseAuthResponse] indicating success or failure.
     */
    fun attemptToCreateAccountFirebaseAuthResponseFlow(
        email: String,
        password: String
    ): Flow<CreateAccountFirebaseAuthResponse>

    /**
     * Creates a user record in Firebase Realtime Database with username and email.
     *
     * @param userName The username for the account.
     * @param email The email for the account.
     * @return [Flow] emitting a [Pair] of success status and the Firebase reference key (nullable if failed).
     */
    fun attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
        userName: String,
        email: String
    ): Flow<Pair<Boolean, String?>>

    /**
     * Stores default shot IDs to ignore for the current user in Firebase Realtime Database.
     *
     * @param defaultShotIdsToIgnore List of shot IDs to ignore.
     * @return [Flow] emitting a [Pair] of success status and the stored list (nullable if failed).
     */
    fun attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(
        defaultShotIdsToIgnore: List<Int>
    ): Flow<Pair<Boolean, List<Int>?>>

    /**
     * Creates a declared shot entry in Firebase Realtime Database.
     *
     * @param declaredShot The declared shot data.
     * @return [Flow] emitting a [Pair] of success status and the generated Firebase key (nullable if failed).
     */
    fun attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(
        declaredShot: DeclaredShot
    ): Flow<Pair<Boolean, String?>>

    /**
     * Adds a player to Firebase Realtime Database.
     *
     * @param playerInfoRealtimeResponse Player information including name, position, image, and shots logged.
     * @return [Flow] emitting a [Pair] of success status and the Firebase key (nullable if failed).
     */
    fun attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
        playerInfoRealtimeResponse: PlayerInfoRealtimeResponse
    ): Flow<Pair<Boolean, String?>>

    /**
     * Adds an individual player report to Firebase Realtime Database.
     *
     * @param individualPlayerReportRealtimeResponse Report information including player name, PDF URL, and logged date.
     * @return [Flow] emitting a [Pair] of success status and the Firebase key (nullable if failed).
     */
    fun attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(
        individualPlayerReportRealtimeResponse: IndividualPlayerReportRealtimeResponse
    ): Flow<Pair<Boolean, String?>>

    /**
     * Uploads an image to Firebase Storage and retrieves its download URL.
     *
     * @param uri The URI of the image to upload.
     * @return [Flow] emitting the download URL as a [String], or null if the upload failed.
     */
    fun attemptToCreateImageFirebaseStorageResponseFlow(uri: Uri): Flow<String?>

    /**
     * Uploads a PDF to Firebase Storage and retrieves its download URL.
     *
     * @param uri The URI of the PDF to upload.
     * @return [Flow] emitting the download URL as a [String], or null if the upload failed.
     */
    fun attemptToCreatePdfFirebaseStorageResponseFlow(uri: Uri): Flow<String?>
}

