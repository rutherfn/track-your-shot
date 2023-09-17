package com.nicholas.rutherford.track.my.shot.firebase.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestCreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.my.shot.firebase.realtime.TestCreateAccountFirebaseRealtimeDatabaseResult
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

class CreateFirebaseUserInfoImplTest {

    private lateinit var createFirebaseUserInfoImpl: CreateFirebaseUserInfoImpl

    private val createFirebaseLastUpdated = mockk<CreateFirebaseLastUpdated>(relaxed = true)
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    private val createAccountResponse = TestCreateAccountFirebaseAuthResponse().create()
    private val createAccountResult = TestCreateAccountFirebaseRealtimeDatabaseResult().create()

    private val testEmail = "testemail@yahoo.com"
    private val testPassword = "passwordTest112"

    @BeforeEach
    fun beforeEach() {
        createFirebaseUserInfoImpl = CreateFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, createFirebaseLastUpdated = createFirebaseLastUpdated, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class AttemptToCreateAccountFirebaseAuthResponseFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed and isSuccessful returns true should set flow to valid create account response flow`() =
            runTest {
                val mockTaskAuthResult = mockk<Task<AuthResult>>()
                val slot = slot<OnCompleteListener<AuthResult>>()

                mockkStatic(Tasks::class)

                every { mockTaskAuthResult.isSuccessful } returns true
                every { mockTaskAuthResult.result!!.additionalUserInfo!!.username } returns createAccountResponse.username
                every { mockTaskAuthResult.result!!.additionalUserInfo!!.isNewUser } returns true
                every { mockTaskAuthResult.exception } returns null

                every {
                    firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword)
                        .addOnCompleteListener(capture(slot))
                } answers {
                    slot.captured.onComplete(mockTaskAuthResult)
                    mockTaskAuthResult
                }

                val createFirebaseUserInfo =
                    createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseAuthResponseFlow(
                        testEmail,
                        testPassword
                    ).first()

                Assertions.assertEquals(
                    createAccountResponse.copy(exception = null),
                    createFirebaseUserInfo
                )
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed and isSuccessful returns false should set flow to valid create account response flow`() =
            runTest {
                val mockException = mockk<java.lang.Exception>(relaxed = true)
                val mockTaskAuthResult = mockk<Task<AuthResult>>()
                val slot = slot<OnCompleteListener<AuthResult>>()

                mockkStatic(Tasks::class)

                every { mockTaskAuthResult.isSuccessful } returns false
                every { mockTaskAuthResult.exception } returns mockException

                every {
                    firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword)
                        .addOnCompleteListener(capture(slot))
                } answers {
                    slot.captured.onComplete(mockTaskAuthResult)
                    mockTaskAuthResult
                }

                val createFirebaseUserInfo =
                    createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseAuthResponseFlow(
                        testEmail,
                        testPassword
                    ).first()

                Assertions.assertEquals(
                    createAccountResponse.copy(isSuccessful = false, username = null, isNewUser = null, exception = mockException),
                    createFirebaseUserInfo
                )
            }
    }

    @Nested
    inner class AttemptToCreateFirebaseRealtimeDatabaseResponseFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete lisetener is executed should set flow to true and value Pair when isSucessful returns back true`() =
            runTest {
                val mockTaskVoidResult = mockk<Task<Void>>()
                val slot = slot<OnCompleteListener<Void>>()

                val values = hashMapOf<String, String>()

                values[Constants.USERNAME] = createAccountResult.username
                values[Constants.EMAIL] = createAccountResult.email

                mockkStatic(Tasks::class)

                every { mockTaskVoidResult.isSuccessful } returns true

                every {
                    firebaseDatabase.getReference(Constants.USERS_PATH)
                        .child(Constants.ACCOUNT_INFO).push().setValue(values)
                        .addOnCompleteListener(capture(slot))
                } answers {
                    slot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(Constants.USERS_PATH)
                    .child(Constants.ACCOUNT_INFO).push()

                every { reference.setValue(values, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                        userName = createAccountResult.username,
                        email = createAccountResult.email
                    ).first()

                Assertions.assertEquals(Pair(true, reference.key), value)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when add on complete listener is executed should set flow to false and null Pair when isSuccessful returns back false`() =
            runTest {
                val mockTaskVoidResult = mockk<Task<Void>>()
                val slot = slot<OnCompleteListener<Void>>()

                val values = hashMapOf<String, String>()

                values[Constants.USERNAME] = createAccountResult.username
                values[Constants.EMAIL] = createAccountResult.email

                mockkStatic(Tasks::class)

                every { mockTaskVoidResult.isSuccessful } returns false

                every {
                    firebaseDatabase.getReference(Constants.USERS_PATH)
                        .child(Constants.ACCOUNT_INFO).push().setValue(values)
                        .addOnCompleteListener(capture(slot))
                } answers {
                    slot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                        userName = createAccountResult.username,
                        email = createAccountResult.email
                    ).first()

                Assertions.assertEquals(Pair(false, null), value)
            }
    }
}
