package com.nicholas.rutherford.track.your.shot.firebase.core.read

import com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.IndividualPlayerReportWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface defining methods to read Firebase user information.
 * Provides reactive [Flow] access to account data, created shots, player info,
 * individual reports, deleted shot IDs, and authentication status.
 */
interface ReadFirebaseUserInfo {

    /**
     * Retrieves the email of the currently logged-in Firebase user.
     *
     * @return [Flow] emitting the user's email if logged in, or null otherwise.
     */
    fun getLoggedInAccountEmail(): Flow<String?>

    /**
     * Retrieves account information (username and email) for the current user.
     *
     * @return [Flow] emitting [AccountInfoRealtimeResponse] or null if unavailable.
     */
    fun getAccountInfoFlow(): Flow<AccountInfoRealtimeResponse?>

    /**
     * Retrieves a list of shot IDs that the user has marked as deleted/ignored.
     *
     * @return [Flow] emitting a list of deleted shot IDs, or empty list if none exist.
     */
    fun getDeletedShotIdsFlow(): Flow<List<Int>>

    /**
     * Retrieves all declared shots created by the user, along with their Firebase keys.
     *
     * @return [Flow] emitting a list of [DeclaredShotWithKeyRealtimeResponse], empty if none exist.
     */
    fun getCreatedDeclaredShotsFlow(): Flow<List<DeclaredShotWithKeyRealtimeResponse>>

    /**
     * Retrieves the Firebase key for the current user's account info.
     *
     * @return [Flow] emitting the account info key, or null if not found.
     */
    fun getAccountInfoKeyFlow(): Flow<String?>

    /**
     * Retrieves all players associated with the current user, including their Firebase keys.
     *
     * @return [Flow] emitting a list of [PlayerInfoRealtimeWithKeyResponse], empty if none exist.
     */
    fun getPlayerInfoList(): Flow<List<PlayerInfoRealtimeWithKeyResponse>>

    /**
     * Retrieves all individual player reports for the current user, including Firebase keys.
     *
     * @return [Flow] emitting a list of [IndividualPlayerReportWithKeyRealtimeResponse], empty if none exist.
     */
    fun getReportList(): Flow<List<IndividualPlayerReportWithKeyRealtimeResponse>>

    /**
     * Retrieves the last updated timestamp for the content in Firebase.
     *
     * @return [Flow] emitting the last update [Date], or null if unavailable.
     */
    fun getLastUpdatedDateFlow(): Flow<Date?>

    /**
     * Checks whether the logged-in user's email is verified.
     *
     * @return [Flow] emitting true if verified, false otherwise.
     */
    fun isEmailVerifiedFlow(): Flow<Boolean>

    /**
     * Checks whether a user is currently logged in.
     *
     * @return [Flow] emitting true if logged in, false otherwise.
     */
    fun isLoggedInFlow(): Flow<Boolean>
}

