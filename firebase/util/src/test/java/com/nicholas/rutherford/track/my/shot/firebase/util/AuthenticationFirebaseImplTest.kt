package com.nicholas.rutherford.track.my.shot.firebase.util

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestAuthenticateUserViaEmailFirebaseResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticationFirebaseImplTest {

    private lateinit var authenticationFirebaseImpl: AuthenticationFirebaseImpl

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    private val authenticateViaEmailFirebaseResponse = TestAuthenticateUserViaEmailFirebaseResponse().create()

    @BeforeEach
    fun beforeEach() {
        authenticationFirebaseImpl = AuthenticationFirebaseImpl(firebaseAuth = firebaseAuth)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when firebaseAuth createUser is set to null should set flow to valid authenticate user via email response flow`() = runTest {
        every { firebaseAuth.currentUser } returns null

        val attemptToSendEmailVerificationForCurrentUser = authenticationFirebaseImpl.attemptToSendEmailVerificationForCurrentUser().first()

        Assertions.assertEquals(authenticateViaEmailFirebaseResponse.copy(isSuccessful = false, isAlreadyAuthenticated = false, isUserExist = false), attemptToSendEmailVerificationForCurrentUser)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when firebaseAuth createUser is not set to null and isEamilVerified set to true should set flow to valid authenticate user via email response floe`() = runTest {
        val mockFirebaseUser = mockk<FirebaseUser>()

        every { firebaseAuth.currentUser } returns mockFirebaseUser
        every { firebaseAuth.currentUser!!.isEmailVerified } returns true

        val attemptToSendEmailVerificationForCurrentUser = authenticationFirebaseImpl.attemptToSendEmailVerificationForCurrentUser().first()

        Assertions.assertEquals(authenticateViaEmailFirebaseResponse.copy(isSuccessful = false, isAlreadyAuthenticated = true, isUserExist = true), attemptToSendEmailVerificationForCurrentUser)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when firebaseAuth createUser is not set to null and isEmailVerified is set to false should set flow to valid authenticate user via email response floe`() = runTest {
        val mockTaskVoidResult = mockk<Task<Void>>()
        val slot = slot<OnCompleteListener<Void>>()
        val mockFirebaseUser = mockk<FirebaseUser>()
        val isSuccessful = true

        every { firebaseAuth.currentUser } returns mockFirebaseUser
        every { firebaseAuth.currentUser!!.isEmailVerified } returns false

        mockkStatic(Tasks::class)

        every { mockTaskVoidResult.isSuccessful } returns isSuccessful

        every { firebaseAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(capture(slot)) } answers {
            slot.captured.onComplete(mockTaskVoidResult)
            mockTaskVoidResult
        }

        val attemptToSendEmailVerificationForCurrentUser = authenticationFirebaseImpl.attemptToSendEmailVerificationForCurrentUser().first()

        Assertions.assertEquals(authenticateViaEmailFirebaseResponse.copy(isSuccessful = isSuccessful, isAlreadyAuthenticated = false, isUserExist = true), attemptToSendEmailVerificationForCurrentUser)
    }
}
