package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
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

class ReadFirebaseUserInfoImplTest {

    lateinit var readFirebaseUserInfoImpl: ReadFirebaseUserInfoImpl

    var firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)
    }

    @Nested
    inner class IsEmailVerified {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerified().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not null but complete listener is not successful should be set to false`() = runTest {
            val mockTaskReloadResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            every { mockTaskReloadResult.isSuccessful } returns false

            mockkStatic(Tasks::class)

            every { firebaseAuth.currentUser!!.reload().addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskReloadResult)
                mockTaskReloadResult
            }

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerified().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not null but complete listener is successful with email verified set to false should be set to false`() = runTest {
            val mockTaskReloadResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            every { firebaseAuth.currentUser!!.isEmailVerified } returns false
            every { mockTaskReloadResult.isSuccessful } returns true

            mockkStatic(Tasks::class)

            every { firebaseAuth.currentUser!!.reload().addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskReloadResult)
                mockTaskReloadResult
            }

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerified().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not null but complete listener is successful with email verified set to true should be set to true`() = runTest {
            val mockTaskReloadResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            every { firebaseAuth.currentUser!!.isEmailVerified } returns true
            every { mockTaskReloadResult.isSuccessful } returns true

            mockkStatic(Tasks::class)

            every { firebaseAuth.currentUser!!.reload().addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskReloadResult)
                mockTaskReloadResult
            }

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isEmailVerified().first())
        }
    }

    @Nested
    inner class IsLoggedIn {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isLoggedIn().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not set to null should be set to true`() = runTest {
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isLoggedIn().first())
        }
    }
}
