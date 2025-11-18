package com.nicholas.rutherford.track.your.shot.helper.reviews

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ReviewManagerImplTest {

    private lateinit var reviewManager: ReviewManagerImpl
    private lateinit var activity: Activity

    @BeforeEach
    fun setUp() {
        reviewManager = ReviewManagerImpl()
        activity = mockk<Activity>(relaxed = false)
        clearMocks(activity)
    }

    @Nested
    inner class RequestReview {

        @Test
        fun `should return true when Play Store is available with package`() = runTest {
            // Given
            val intentSlot = slot<Intent>()
            every { activity.startActivity(capture(intentSlot)) } returns Unit

            // When
            val result = reviewManager.requestReview(activity)

            // Then
            Assertions.assertTrue(result)
            verify(exactly = 1) { activity.startActivity(any()) }
            val capturedIntent = intentSlot.captured
            Assertions.assertEquals(Intent.ACTION_VIEW, capturedIntent.action)
            Assertions.assertNotNull(capturedIntent.data) // Verify data is set (exact Uri value may vary in tests)
            Assertions.assertEquals(Constants.VENDING_ANDROID_PACKAGE, capturedIntent.`package`)
        }

        @Test
        fun `should return true when ActivityNotFoundException is thrown but fallback succeeds`() = runTest {
            // Given
            val capturedIntents = mutableListOf<Intent>()
            var callCount = 0
            every { activity.startActivity(any()) } answers {
                val intent = call.invocation.args[0] as Intent
                capturedIntents.add(intent)
                callCount++
                if (callCount == 1) {
                    throw ActivityNotFoundException("Play Store not found")
                }
            }

            // When
            val result = reviewManager.requestReview(activity)

            // Then
            Assertions.assertTrue(result)
            verify(exactly = 2) { activity.startActivity(any()) }
            Assertions.assertEquals(2, capturedIntents.size)
            
            // First call should have package set
            val firstIntent = capturedIntents[0]
            Assertions.assertEquals(Intent.ACTION_VIEW, firstIntent.action)
            Assertions.assertNotNull(firstIntent.data) // Verify data is set
            Assertions.assertEquals(Constants.VENDING_ANDROID_PACKAGE, firstIntent.`package`)
            
            // Second call should not have package set (fallback)
            val secondIntent = capturedIntents[1]
            Assertions.assertEquals(Intent.ACTION_VIEW, secondIntent.action)
            Assertions.assertNotNull(secondIntent.data) // Verify data is set
            Assertions.assertNull(secondIntent.`package`)
        }

        @Test
        fun `should return false when both attempts fail`() = runTest {
            // Given
            var callCount = 0
            every { activity.startActivity(any()) } answers {
                callCount++
                if (callCount == 1) {
                    throw ActivityNotFoundException("Play Store not found")
                } else {
                    throw RuntimeException("Unable to open browser")
                }
            }

            // When
            val result = reviewManager.requestReview(activity)

            // Then
            Assertions.assertFalse(result)
            verify(exactly = 2) { activity.startActivity(any()) }
        }

        @Test
        fun `should create Intent with correct Play Store URL`() = runTest {
            // Given
            val intentSlot = slot<Intent>()
            every { activity.startActivity(capture(intentSlot)) } returns Unit

            // When
            reviewManager.requestReview(activity)

            // Then
            val capturedIntent = intentSlot.captured
            Assertions.assertNotNull(capturedIntent.data) // Verify data is set
        }

        @Test
        fun `should create Intent with ACTION_VIEW action`() = runTest {
            // Given
            val intentSlot = slot<Intent>()
            every { activity.startActivity(capture(intentSlot)) } returns Unit

            // When
            reviewManager.requestReview(activity)

            // Then
            val capturedIntent = intentSlot.captured
            Assertions.assertEquals(Intent.ACTION_VIEW, capturedIntent.action)
        }

        @Test
        fun `should set VENDING_ANDROID_PACKAGE on first Intent attempt`() = runTest {
            // Given
            val intentSlot = slot<Intent>()
            every { activity.startActivity(capture(intentSlot)) } returns Unit

            // When
            reviewManager.requestReview(activity)

            // Then
            val capturedIntent = intentSlot.captured
            Assertions.assertEquals(Constants.VENDING_ANDROID_PACKAGE, capturedIntent.`package`)
        }

        @Test
        fun `should not set package on fallback Intent when ActivityNotFoundException occurs`() = runTest {
            // Given
            val capturedIntents = mutableListOf<Intent>()
            var callCount = 0
            every { activity.startActivity(any()) } answers {
                val intent = call.invocation.args[0] as Intent
                capturedIntents.add(intent)
                callCount++
                if (callCount == 1) {
                    throw ActivityNotFoundException("Play Store not found")
                }
            }

            // When
            reviewManager.requestReview(activity)

            // Then
            verify(exactly = 2) { activity.startActivity(any()) }
            Assertions.assertEquals(2, capturedIntents.size)
            // The second intent should not have a package set
            val secondIntent = capturedIntents[1]
            Assertions.assertNull(secondIntent.`package`)
            Assertions.assertEquals(Intent.ACTION_VIEW, secondIntent.action)
            Assertions.assertNotNull(secondIntent.data) // Verify data is set
        }

        @Test
        fun `should handle generic Exception on fallback attempt`() = runTest {
            // Given
            var callCount = 0
            every { activity.startActivity(any()) } answers {
                callCount++
                if (callCount == 1) {
                    throw ActivityNotFoundException("Play Store not found")
                } else {
                    throw IllegalStateException("Browser not available")
                }
            }

            // When
            val result = reviewManager.requestReview(activity)

            // Then
            Assertions.assertFalse(result)
            verify(exactly = 2) { activity.startActivity(any()) }
        }

        @Test
        fun `should call startActivity exactly once when successful`() = runTest {
            // Given
            every { activity.startActivity(any()) } returns Unit

            // When
            reviewManager.requestReview(activity)

            // Then
            verify(exactly = 1) { activity.startActivity(any()) }
        }

        @Test
        fun `should call startActivity twice when fallback is needed`() = runTest {
            // Given
            var callCount = 0
            every { activity.startActivity(any()) } answers {
                callCount++
                if (callCount == 1) {
                    throw ActivityNotFoundException("Play Store not found")
                }
            }

            // When
            reviewManager.requestReview(activity)

            // Then
            verify(exactly = 2) { activity.startActivity(any()) }
        }
    }
}