package com.nicholas.rutherford.track.my.shot.firebase.core.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.firebase.core.create.CreateFirebaseLastUpdatedImpl
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date

class CreateFirebaseLastUpdatedImplTest {

    private lateinit var createLastUpdatedImpl: CreateFirebaseLastUpdatedImpl

    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    private val lastUpdatedDate = Date()

    @BeforeEach
    fun beforeEach() {
        createLastUpdatedImpl = CreateFirebaseLastUpdatedImpl(firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class AttemptToCreateAccountFirebaseAuthResponseFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed should set flow to false when isSuccessful returns back false`() = runTest {
            val mockTaskVoidResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            val values = hashMapOf<String, Long>()

            values[Constants.LAST_UPDATED] = lastUpdatedDate.time

            mockkStatic(Tasks::class)

            every { mockTaskVoidResult.isSuccessful } returns false

            every { firebaseDatabase.reference.child(Constants.CONTENT_LAST_UPDATED_PATH).setValue(values).addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createLastUpdatedImpl.attemptToCreateLastUpdatedFlow(date = lastUpdatedDate).first()

            Assertions.assertEquals(false, value)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed should set flow to true when isSuccessful returns back true`() = runTest {
            val mockTaskVoidResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            val values = hashMapOf<String, Long>()

            values[Constants.LAST_UPDATED] = lastUpdatedDate.time

            mockkStatic(Tasks::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every { firebaseDatabase.reference.child(Constants.CONTENT_LAST_UPDATED_PATH).setValue(values).addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createLastUpdatedImpl.attemptToCreateLastUpdatedFlow(date = lastUpdatedDate).first()

            Assertions.assertEquals(true, value)
        }
    }
}
