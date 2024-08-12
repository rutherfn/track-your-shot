package com.nicholas.rutherford.track.your.shot.firebase.core.delete

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

class DeleteFirebaseUserInfoImplTest {

    private lateinit var deleteFirebaseUserInfoImpl: DeleteFirebaseUserInfoImpl

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    private val firebasePlayerKey = "-zRTW11129"

    @BeforeEach
    fun beforeEach() {
        deleteFirebaseUserInfoImpl = DeleteFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class DeletePlayer {

        @Test
        fun `when add on failure listener is executed should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}/$firebasePlayerKey"

            val mockException = Exception("Simulated failure")

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val slot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .removeValue()
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val value = deleteFirebaseUserInfoImpl.deletePlayer(playerKey = firebasePlayerKey).first()

            Assertions.assertEquals(false, value)
        }

        @Test
        fun `when add on complete listener is executed and returns isSuccessful returns true should set flow to true`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}/$firebasePlayerKey"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val slot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .removeValue()
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = deleteFirebaseUserInfoImpl.deletePlayer(playerKey = firebasePlayerKey).first()

            Assertions.assertEquals(true, value)
        }
    }
}
