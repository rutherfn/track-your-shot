package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestCreateAccountResponse
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

class CreateFirebaseUserInfoImplTest {

    lateinit var createFirebaseUserInfoImpl: CreateFirebaseUserInfoImpl

    val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    val createAccountResponse = TestCreateAccountResponse().create()

    val testEmail = "testemail@yahoo.com"
    val testPassword = "passwordTest112"

    @BeforeEach
    fun beforeEach() {
        createFirebaseUserInfoImpl = CreateFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when add on complete listener is executed should set flow to valid create account response flow`() = runTest {
        val mockTaskAuthResult = mockk<Task<AuthResult>>()
        val slot = slot<OnCompleteListener<AuthResult>>()

        mockkStatic(Tasks::class)

        every { mockTaskAuthResult.isSuccessful } returns createAccountResponse.isSuccessful
        every { mockTaskAuthResult.result!!.additionalUserInfo!!.username } returns createAccountResponse.username
        every { mockTaskAuthResult.result!!.additionalUserInfo!!.isNewUser } returns true
        every { mockTaskAuthResult.exception } returns null

        every { firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword).addOnCompleteListener(capture(slot)) } answers {
            slot.captured.onComplete(mockTaskAuthResult)
            mockTaskAuthResult
        }

        val createFirebaseUserInfo = createFirebaseUserInfoImpl.attemptToCreateAccountResponseFlow(testEmail, testPassword).first()

        Assertions.assertEquals(createAccountResponse.copy(exception = null), createFirebaseUserInfo)
    }
}
