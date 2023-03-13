package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
    var firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Test
    fun constants() {
        Assertions.assertEquals(ACCOUNT_INFO, "accountInfo")
        Assertions.assertEquals(EMAIL, "email")
        Assertions.assertEquals(USERS, "users")
    }

    @Nested
    inner class GetLoggedInAccountEmail {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to null`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser email is set to null should set to null`() = runTest {
            every { firebaseAuth.currentUser!!.email } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser and email is not set to null should set value to returned email`() = runTest {
            val email = "testEmail@yahoo.com"

            every { firebaseAuth.currentUser!!.email } returns email
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(email, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }
    }

    @Nested
    inner class IsEmailVerifiedFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
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

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
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

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
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

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
        }
    }

    @Nested
    inner class IsLoggedInFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isLoggedInFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not set to null should be set to true`() = runTest {
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isLoggedInFlow().first())
        }
    }
}
