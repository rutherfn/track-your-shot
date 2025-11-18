package com.nicholas.rutherford.track.your.shot.helper.reviews

/**
 * Created by Nicholas Rutherford, last edited on 2025-11-09
 *
 * Manages when to show review prompts to users based on app usage patterns.
 * Follows Google's best practices for in-app review prompts:
 * - Only prompts after user has experienced the app (minimum launch count)
 * - Respects time between prompts
 * - Honors user's decision if they decline
 */
interface ReviewPromptManager {
    /**
     * Checks if a review prompt should be shown based on app usage.
     * Increments launch count and evaluates criteria.
     *
     * @return true if review prompt should be shown, false otherwise.
     */
    suspend fun shouldShowReviewPrompt(): Boolean

    /**
     * Records that a review prompt was shown.
     */
    suspend fun recordReviewPromptShown()

    /**
     * Records that the user declined to review.
     */
    suspend fun recordUserDeclinedReview()

    /**
     * Resets all review tracking data (useful for testing).
     * Only works in debug builds.
     */
    suspend fun resetReviewTracking()
}

