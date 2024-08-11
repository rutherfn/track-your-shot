package com.nicholas.rutherford.track.your.shot.firebase.core.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.firebase.TestCreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestCreateAccountFirebaseRealtimeDatabaseResult
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeResponse
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

class CreateFirebaseUserInfoImplTest {

    private lateinit var createFirebaseUserInfoImpl: CreateFirebaseUserInfoImpl

    private val createFirebaseLastUpdated = mockk<CreateFirebaseLastUpdated>(relaxed = true)
    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)
    private val firebaseStorage = mockk<FirebaseStorage>(relaxed = true)

    private val createAccountResponse = TestCreateAccountFirebaseAuthResponse().create()
    private val createAccountResult = TestCreateAccountFirebaseRealtimeDatabaseResult().create()
    private val playerInfoRealtimeResponse = TestPlayerInfoRealtimeResponse().create()

    private val testEmail = "testemail@yahoo.com"
    private val testPassword = "passwordTest112"

    private val key = "-ATT82121"

    @BeforeEach
    fun beforeEach() {
        createFirebaseUserInfoImpl = CreateFirebaseUserInfoImpl(
            firebaseAuth = firebaseAuth,
            createFirebaseLastUpdated = createFirebaseLastUpdated,
            firebaseDatabase = firebaseDatabase,
            firebaseStorage = firebaseStorage
        )
    }

    @Nested
    inner class AttemptToCreateAccountFirebaseAuthResponseFlow {

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
                    createAccountResponse.copy(
                        isSuccessful = false,
                        username = null,
                        isNewUser = null,
                        exception = mockException
                    ),
                    createFirebaseUserInfo
                )
            }
    }

    @Nested
    inner class AttemptToCreateFirebaseRealtimeDatabaseResponseFlow {

        @Test
        fun `when add on complete listener is executed should set flow to true and value Pair when isSuccessful returns back true`() =
            runTest {
                val uid = "uid"
                val path = "${Constants.USERS_PATH}/$uid"

                val mockTaskVoidResult = mockk<Task<Void>>()
                val mockFirebaseUser = mockk<FirebaseUser>()
                val slot = slot<OnCompleteListener<Void>>()

                val values = hashMapOf<String, String>()

                values[Constants.USERNAME] = createAccountResult.username
                values[Constants.EMAIL] = createAccountResult.email

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockTaskVoidResult.isSuccessful } returns true

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every {
                    firebaseDatabase.getReference(path).push().setValue(values)
                        .addOnCompleteListener(capture(slot))
                } answers {
                    slot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(path).push()

                every { reference.setValue(values, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                        userName = createAccountResult.username,
                        email = createAccountResult.email
                    ).first()

                Assertions.assertEquals(Pair(true, uid), value)
            }

        @Test
        fun `when add on complete listener is executed should set flow to false and null Pair when isSuccessful returns back false`() =
            runTest {
                val uid = "uid"
                val path = "${Constants.USERS_PATH}/$uid"

                val mockTaskVoidResult = mockk<Task<Void>>()
                val mockFirebaseUser = mockk<FirebaseUser>()
                val slot = slot<OnCompleteListener<Void>>()

                val values = hashMapOf<String, String>()

                values[Constants.USERNAME] = createAccountResult.username
                values[Constants.EMAIL] = createAccountResult.email

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every { mockTaskVoidResult.isSuccessful } returns false

                every {
                    firebaseDatabase.getReference(path).push().setValue(values)
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

    @Nested
    inner class AttemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow {

        @Test
        fun `when add on complete listener is executed should set flow to false when isSuccessful returns back false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val slot = slot<OnCompleteListener<Void>>()

            val values = hashMapOf<String, Any>()

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl
            values[Constants.SHOTS_LOGGED] = playerInfoRealtimeResponse.shotsLogged

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every { mockTaskVoidResult.isSuccessful } returns false

            every {
                firebaseDatabase.getReference(path).push().setValue(values).addOnCompleteListener(capture(slot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
                key = key,
                playerInfoRealtimeResponse = playerInfoRealtimeResponse
            ).first()

            Assertions.assertEquals(false, value)
        }

        @Test
        fun `when add on complete listener is executed should set flow to true when isSuccessful returns back true`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val slot = slot<OnCompleteListener<Void>>()

            val values = hashMapOf<String, Any>()

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl
            values[Constants.SHOTS_LOGGED] = playerInfoRealtimeResponse.shotsLogged

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every { mockTaskVoidResult.isSuccessful } returns true

            every {
                firebaseDatabase.getReference(path).push().setValue(values).addOnCompleteListener(capture(slot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
                key = key,
                playerInfoRealtimeResponse = playerInfoRealtimeResponse
            ).first()

            Assertions.assertEquals(true, value)
        }
    }
}
