package com.nicholas.rutherford.track.your.shot.helper.reviews

import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.data.store.reader.DataStorePreferencesReader
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExt
import kotlinx.coroutines.flow.first

/**
 * Created by Nicholas Rutherford, last edited on 2025-11-09
 *
 * Implementation of [ReviewPromptManager] that determines when to show review prompts
 * based on app launch count, time since last prompt, and user's previous decisions.
 */
class ReviewPromptManagerImpl(
    private val dataStoreReader: DataStorePreferencesReader,
    private val dataStoreWriter: DataStorePreferencesWriter,
    private val buildType: BuildType,
    private val dateExt: DateExt
) : ReviewPromptManager {

    internal fun buildNewLaunchCount(currentLaunchCount: Int, incrementValue: Int): Int = currentLaunchCount + incrementValue

    internal fun buildMinLaunchCount(isDebug: Boolean): Int {
        return if (isDebug) {
            Constants.ReviewPrompt.DEBUG_MIN_LAUNCH_COUNT
        } else {
            Constants.ReviewPrompt.MIN_LAUNCH_COUNT
        }
    }

    internal fun buildTimeDivisor(isDebug: Boolean): Long {
        return if (isDebug) {
            (1000 * 60).toLong()
        } else {
            (1000 * 60 * 60 * 24).toLong()
        }
    }

    internal fun buildTimeSinceLastPrompt(lastPromptDateValue: Long, isDebug: Boolean): Long {
        return if (lastPromptDateValue > 0) {
            (dateExt.now - lastPromptDateValue) / buildTimeDivisor(isDebug = isDebug)
        } else {
            Long.MAX_VALUE
        }
    }

    internal fun buildMinTimeToWait(hasUserDeclined: Boolean, isDebug: Boolean): Long {
        return if (hasUserDeclined) {
            if (isDebug) {
                1L // 1 minute in debug
            } else {
                Constants.ReviewPrompt.DAYS_AFTER_DECLINE.toLong()
            }
        } else {
            if (isDebug) {
                0L // No wait in debug
            } else {
                Constants.ReviewPrompt.MIN_DAYS_BETWEEN_PROMPTS.toLong()
            }
        }
    }

    /**
     * Checks if a review prompt should be shown based on app usage.
     * Increments launch count and evaluates criteria.
     *
     * @return true if review prompt should be shown, false otherwise.
     */
    override suspend fun shouldShowReviewPrompt(): Boolean {
        val newLaunchCount = buildNewLaunchCount(
            currentLaunchCount = dataStoreReader.readAppLaunchCountFlow().first(),
            incrementValue = 1
        )

        dataStoreWriter.saveAppLaunchCount(value = newLaunchCount)

        if (newLaunchCount < buildMinLaunchCount(isDebug = buildType.isDebug())) {
            return false
        } else {
            return buildTimeSinceLastPrompt(
                lastPromptDateValue = dataStoreReader.readLastReviewPromptDateFlow().first(),
                isDebug = buildType.isDebug()
            ) >= buildMinTimeToWait(
                hasUserDeclined = dataStoreReader.readUserDeclinedReviewFlow().first(),
                isDebug = buildType.isDebug()
            )
        }
    }

    /**
     * Records that a review prompt was shown.
     */
    override suspend fun recordReviewPromptShown() {
        dataStoreWriter.saveLastReviewPromptDate(System.currentTimeMillis())
        dataStoreWriter.saveUserDeclinedReview(false)
    }

    /**
     * Records that the user declined to review.
     */
    override suspend fun recordUserDeclinedReview() {
        dataStoreWriter.saveUserDeclinedReview(true)
        dataStoreWriter.saveLastReviewPromptDate(value = System.currentTimeMillis())
    }

    /**
     * Resets all review tracking data (useful for testing).
     */
    override suspend fun resetReviewTracking() {
        dataStoreWriter.saveAppLaunchCount(value = 0)
        dataStoreWriter.saveLastReviewPromptDate(value = 0L)
        dataStoreWriter.saveUserDeclinedReview(value = false)
    }
}

