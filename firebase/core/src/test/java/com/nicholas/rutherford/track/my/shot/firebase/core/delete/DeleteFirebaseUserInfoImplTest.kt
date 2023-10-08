package com.nicholas.rutherford.track.my.shot.firebase.core.delete

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
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

class DeleteFirebaseUserInfoImplTest {

    private lateinit var deleteFirebaseUserInfoImpl: DeleteFirebaseUserInfoImpl

    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    val firebaseAccountKey = "-kTE1212D"
    val firebasePlayerKey = "-zRTW11129"

    @BeforeEach
    fun beforeEach() {
        deleteFirebaseUserInfoImpl = DeleteFirebaseUserInfoImpl(firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class DeletePlayer {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed and returns isSuccessful returns true should set flow to true`() = runTest {
            val mockTaskVoidResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            mockkStatic(Tasks::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .child(firebasePlayerKey)
                    .removeValue()
                    .addOnCompleteListener(capture(slot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = deleteFirebaseUserInfoImpl.deletePlayer(
                accountKey = firebaseAccountKey,
                playerKey = firebasePlayerKey
            ).first()

            Assertions.assertEquals(true, value)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed and returns isSuccessful returns back false should set flow to false`() = runTest {
            val mockTaskVoidResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            mockkStatic(Tasks::class)

            every { mockTaskVoidResult.isSuccessful } returns false

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .child(firebasePlayerKey)
                    .removeValue()
                    .addOnCompleteListener(capture(slot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = deleteFirebaseUserInfoImpl.deletePlayer(
                accountKey = firebaseAccountKey,
                playerKey = firebasePlayerKey
            ).first()

            Assertions.assertEquals(false, value)
        }
    }
}
