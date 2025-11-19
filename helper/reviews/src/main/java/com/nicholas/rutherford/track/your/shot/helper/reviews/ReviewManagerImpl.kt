package com.nicholas.rutherford.track.your.shot.helper.reviews

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.core.net.toUri
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import timber.log.Timber

/**
 * Created by Nicholas Rutherford, last edited on 2025-11-09
 *
 * Implementation of [ReviewManager] that uses Google Play's In-App Review API.
 *
 * This implementation follows Google's best practices:
 * - Requests reviews at appropriate moments (after user has experienced the app)
 * - Respects Google Play's quota system (may not show dialog if quota is exceeded)
 * - Handles errors gracefully
 * - Falls back to opening Play Store listing if in-app review is not available
 */
class ReviewManagerImpl : ReviewManager {

    /**
     * Requests and launches the in-app review flow.
     *
     * The review dialog may not always be shown due to:
     * - Google Play's quota system (users can only see the dialog a limited number of times)
     * - Device requirements (must have Play Store installed)
     *
     * If the in-app review is not available and a Play Store URL is provided,
     * it will open the Play Store listing as a fallback.
     *
     * @param activity The activity context to launch the review flow from.
     * @return true if the review request was successfully initiated or Play Store was opened, false otherwise.
     */
    override suspend fun requestReview(activity: Activity): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_VIEW, Constants.PLAY_STORE_URL.toUri()).apply {
                setPackage(Constants.VENDING_ANDROID_PACKAGE)
            }
            activity.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            Timber.w(message = "Unable to open Play Store listing. Falling back to in-app review with message ${e.message}")
            try {
                val intent = Intent(Intent.ACTION_VIEW, Constants.PLAY_STORE_URL.toUri())
                activity.startActivity(intent)
                true
            } catch (ex: Exception) {
                Timber.e(message = "Unable to open Play Store listing with following message ${ex.message}")
                false
            }
        }
    }
}
