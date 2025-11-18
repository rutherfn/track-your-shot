package com.nicholas.rutherford.track.your.shot.helper.reviews

import android.app.Activity

/**
 * Created by Nicholas Rutherford, last edited on 2025-11-09
 *
 * Responsible for managing Google Play In-App Reviews.
 * Provides a way to request in-app reviews from users.
 */
interface ReviewManager {

    /**
     * Requests and launches the in-app review flow.
     * The review dialog may not always be shown due to Google Play quotas.
     * If the in-app review is not available, falls back to opening the Play Store listing.
     *
     * @param activity The activity context to launch the review flow from.
     * @return true if the review request was successfully initiated, false otherwise.
     */
    suspend fun requestReview(activity: Activity): Boolean
}

