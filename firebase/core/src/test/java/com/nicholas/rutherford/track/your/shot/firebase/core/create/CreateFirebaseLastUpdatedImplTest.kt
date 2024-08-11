package com.nicholas.rutherford.track.your.shot.firebase.core.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date

class CreateFirebaseLastUpdatedImplTest {

    private lateinit var createLastUpdatedImpl: CreateFirebaseLastUpdatedImpl

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    private val lastUpdatedDate = Date()

    @BeforeEach
    fun beforeEach() {
        createLastUpdatedImpl = CreateFirebaseLastUpdatedImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class AttemptToCreateAccountFirebaseAuthResponseFlow {

        @Test
        fun `when add on failure listener is executed should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.CONTENT_LAST_UPDATED_PATH}/$uid"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val slot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val mockException = Exception("Simulated failure")

            val values = hashMapOf<String, Long>()

            values[Constants.LAST_UPDATED] = lastUpdatedDate.time

            mockkStatic(Tasks::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path).setValue(values)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val value = createLastUpdatedImpl.attemptToCreateLastUpdatedFlow(date = lastUpdatedDate).first()

            Assertions.assertEquals(false, value)
        }

        @Test
        fun `when add on complete listener is executed and isSuccessful returns true should set flow to true when isSuccessful returns back true`() = runTest {
            val uid = "uid"
            val path = "${Constants.CONTENT_LAST_UPDATED_PATH}/$uid"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val slot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Long>()

            values[Constants.LAST_UPDATED] = lastUpdatedDate.time

            mockkStatic(Tasks::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path).setValue(values)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createLastUpdatedImpl.attemptToCreateLastUpdatedFlow(date = lastUpdatedDate).first()

            Assertions.assertEquals(true, value)
        }
    }
}
