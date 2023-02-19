package com.nicholas.rutherford.track.my.shot.firebase.util.existinguser

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ExistingUserFirebaseImplTest {

    private lateinit var existingUserFirebaseImpl: ExistingUserFirebaseImpl

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        existingUserFirebaseImpl = ExistingUserFirebaseImpl(firebaseAuth = firebaseAuth)
    }

    @Test
    fun `logout should call firebaseAuth signOut`() {
        existingUserFirebaseImpl.logOut()

        verify { firebaseAuth.signOut() }
    }

    @Nested
    inner class LoginInFlow {

        private val testEmail = "test@email.com"
        private val testPassword = "testPassword123"

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed should set to flow to true when isSuccessful returns back true`() =
            runTest {
                val mockAuthResult = mockk<Task<AuthResult>>()
                val slot = slot<OnCompleteListener<AuthResult>>()

                every { mockAuthResult.isSuccessful } returns true

                every { firebaseAuth.signInWithEmailAndPassword(testEmail, testPassword).addOnCompleteListener(capture(slot)) } answers {
                    slot.captured.onComplete(mockAuthResult)
                    mockAuthResult
                }

                val value = existingUserFirebaseImpl.logInFlow(email = testEmail, password = testPassword).first()

                Assertions.assertEquals(true, value)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed should set to flow to false when isSuccessful returns back false`() =
            runTest {
                val mockAuthResult = mockk<Task<AuthResult>>()
                val slot = slot<OnCompleteListener<AuthResult>>()

                every { mockAuthResult.isSuccessful } returns false

                every { firebaseAuth.signInWithEmailAndPassword(testEmail, testPassword).addOnCompleteListener(capture(slot)) } answers {
                    slot.captured.onComplete(mockAuthResult)
                    mockAuthResult
                }

                val value = existingUserFirebaseImpl.logInFlow(email = testEmail, password = testPassword).first()

                Assertions.assertEquals(false, value)
            }
    }
}
