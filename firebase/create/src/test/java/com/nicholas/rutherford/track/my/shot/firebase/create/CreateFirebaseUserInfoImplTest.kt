package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestCreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestCreateAccountFirebaseRealtimeDatabaseResult
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

class CreateFirebaseUserInfoImplTest {

    lateinit var createFirebaseUserInfoImpl: CreateFirebaseUserInfoImpl

    val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    val createAccountResponse = TestCreateAccountFirebaseAuthResponse().create()
    val createAccountResult = TestCreateAccountFirebaseRealtimeDatabaseResult().create()

    val testEmail = "testemail@yahoo.com"
    val testPassword = "passwordTest112"

    @BeforeEach
    fun beforeEach() {
        createFirebaseUserInfoImpl = CreateFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class Consts {

        @Test
        fun `users path`() {
            Assertions.assertEquals("users", USERS_PATH)
        }

        @Test
        fun `user name`() {
            Assertions.assertEquals("userName", USERNAME)
        }

        @Test
        fun email() {
            Assertions.assertEquals("email", EMAIL)
        }

        @Test
        fun `user info`() {
            Assertions.assertEquals("userInfo", USER_INFO)
        }
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

        val createFirebaseUserInfo = createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseAuthResponseFlow(testEmail, testPassword).first()

        Assertions.assertEquals(createAccountResponse.copy(exception = null), createFirebaseUserInfo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when add on complete listener is executed should set flow to true when isSuccessful returns back true`() = runTest {
        val mockTaskVoidResult = mockk<Task<Void>>()
        val slot = slot<OnCompleteListener<Void>>()

        val values = hashMapOf<String, String>()

        values[USERNAME] = createAccountResult.username
        values[EMAIL] = createAccountResult.email

        mockkStatic(Tasks::class)

        every { mockTaskVoidResult.isSuccessful } returns false

        every { firebaseDatabase.getReference(USERS_PATH).child(USER_INFO).push().setValue(values).addOnCompleteListener(capture(slot)) } answers {
            slot.captured.onComplete(mockTaskVoidResult)
            mockTaskVoidResult
        }

        val value = createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(userName = createAccountResult.username, email = createAccountResult.email).first()

        Assertions.assertEquals(false, value)
    }
}
