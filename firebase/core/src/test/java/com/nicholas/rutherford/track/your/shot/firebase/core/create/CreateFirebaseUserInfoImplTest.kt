package com.nicholas.rutherford.track.your.shot.firebase.core.create

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.firebase.TestCreateAccountFirebaseAuthResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestCreateAccountFirebaseRealtimeDatabaseResult
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestIndividualPlayerReportRealtimeResponse
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

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)
    private val firebaseStorage = mockk<FirebaseStorage>(relaxed = true)

    private val createAccountResponse = TestCreateAccountFirebaseAuthResponse().create()
    private val createAccountResult = TestCreateAccountFirebaseRealtimeDatabaseResult().create()
    private val playerInfoRealtimeResponse = TestPlayerInfoRealtimeResponse().create()
    private val individualPlayerReportRealtimeResponse = TestIndividualPlayerReportRealtimeResponse.create()

    private val testEmail = "testemail@yahoo.com"
    private val testPassword = "passwordTest112"

    private val key = "-ATT82121"

    @BeforeEach
    fun beforeEach() {
        createFirebaseUserInfoImpl = CreateFirebaseUserInfoImpl(
            firebaseAuth = firebaseAuth,
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
                val failureListenerSlot = slot<OnFailureListener>()

                mockkStatic(Tasks::class)

                every { mockTaskAuthResult.isSuccessful } returns true
                every { mockTaskAuthResult.result!!.additionalUserInfo!!.username } returns createAccountResponse.username
                every { mockTaskAuthResult.result!!.additionalUserInfo!!.isNewUser } returns true
                every { mockTaskAuthResult.exception } returns null

                every {
                    firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword)
                        .addOnCompleteListener(capture(slot))
                        .addOnFailureListener(capture(failureListenerSlot))
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
        fun `when add on failure listener is executed should set flow to valid create account response flow`() =
            runTest {
                val mockTaskAuthResult = mockk<Task<AuthResult>>()
                val completeListenerSlot = slot<OnCompleteListener<AuthResult>>()
                val failureListenerSlot = slot<OnFailureListener>()

                mockkStatic(FirebaseAuth::class)

                val mockException = Exception("Simulated failure")

                every {
                    firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword)
                        .addOnCompleteListener(capture(completeListenerSlot))
                        .addOnFailureListener(capture(failureListenerSlot))
                } answers {
                    failureListenerSlot.captured.onFailure(mockException)
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
    inner class AttemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow {

        @Test
        fun `when add on complete listener is executed should set flow a list and value Pair when isSuccessful returns back true`() =
            runTest {
                val uid = "uid"
                val path = "${Constants.USERS_PATH}/$uid/${Constants.SHOT_IDS_TO_IGNORE}"
                val defaultShotIdsToIgnore = listOf(11, 22, 44, 2)

                val mockTaskVoidResult = mockk<Task<Void>>()
                val mockFirebaseUser = mockk<FirebaseUser>()
                val onCompleteListenerSlot = slot<OnCompleteListener<Void>>()
                val failureListenerSlot = slot<OnFailureListener>()

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockTaskVoidResult.isSuccessful } returns true

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every { firebaseDatabase.getReference(path).key } returns key

                every {
                    firebaseDatabase.getReference(path).setValue(defaultShotIdsToIgnore)
                        .addOnCompleteListener(capture(onCompleteListenerSlot))
                        .addOnFailureListener(capture(failureListenerSlot))
                } answers {
                    onCompleteListenerSlot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(path).push()

                every { reference.setValue(defaultShotIdsToIgnore, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore = defaultShotIdsToIgnore).first()

                Assertions.assertEquals(Pair(true, defaultShotIdsToIgnore), value)
            }

        @Test
        fun `when add on failure listener is executed should set flow to false and null Pair`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.SHOT_IDS_TO_IGNORE}"
            val defaultShotIdsToIgnore = listOf(11, 22, 44, 2)

            val mockException = Exception("Simulated failure")

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path).setValue(defaultShotIdsToIgnore)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val reference = firebaseDatabase.getReference(path).push()

            every { reference.setValue(defaultShotIdsToIgnore, any()) }

            val value =
                createFirebaseUserInfoImpl.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore = defaultShotIdsToIgnore).first()

            Assertions.assertEquals(Pair(false, null), value)
        }

        @Test
        fun `when add on complete listener is executed should set flow to a null and value Pair of false when isSuccessful returns back false`() =
            runTest {
                val uid = "uid"
                val path = "${Constants.USERS_PATH}/$uid/${Constants.SHOT_IDS_TO_IGNORE}"
                val defaultShotIdsToIgnore = listOf(11, 22, 44, 2)

                val mockTaskVoidResult = mockk<Task<Void>>()
                val mockFirebaseUser = mockk<FirebaseUser>()
                val onCompleteListenerSlot = slot<OnCompleteListener<Void>>()
                val failureListenerSlot = slot<OnFailureListener>()

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockTaskVoidResult.isSuccessful } returns false

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every { firebaseDatabase.getReference(path).key } returns key

                every {
                    firebaseDatabase.getReference(path).setValue(defaultShotIdsToIgnore)
                        .addOnCompleteListener(capture(onCompleteListenerSlot))
                        .addOnFailureListener(capture(failureListenerSlot))
                } answers {
                    onCompleteListenerSlot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(path).push()

                every { reference.setValue(defaultShotIdsToIgnore, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore = defaultShotIdsToIgnore).first()

                Assertions.assertEquals(Pair(false, null), value)
            }
    }

    @Nested
    inner class AttemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow {

        @Test
        fun `when add on complete listener is executed should set flow to true and value Pair of declared shot when isSuccessful returns back true`() =
            runTest {
                val declaredShot = DeclaredShot(
                    id = 0,
                    shotCategory = "category",
                    title = "title",
                    description = "description"
                )
                val uid = "uid"
                val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}"

                val mockTaskVoidResult = mockk<Task<Void>>()
                val mockFirebaseUser = mockk<FirebaseUser>()
                val onCompleteListenerSlot = slot<OnCompleteListener<Void>>()
                val failureListenerSlot = slot<OnFailureListener>()

                val values = hashMapOf<String, Any>()

                values[Constants.ID] = declaredShot.id
                values[Constants.SHOT_CATEGORY] = declaredShot.shotCategory
                values[Constants.TITLE] = declaredShot.title
                values[Constants.DESCRIPTION] = declaredShot.description

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockTaskVoidResult.isSuccessful } returns true

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every { firebaseDatabase.getReference(path).key } returns key

                every {
                    firebaseDatabase.getReference(path).setValue(values)
                        .addOnCompleteListener(capture(onCompleteListenerSlot))
                        .addOnFailureListener(capture(failureListenerSlot))
                } answers {
                    onCompleteListenerSlot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(path).push()

                val key = "mockKey"
                every { reference.push().key } returns key

                every { reference.child(key).setValue(values, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(
                        declaredShot = declaredShot
                    ).first()

                Assertions.assertEquals(Pair(true, declaredShot), value)
            }

        @Test
        fun `when add on complete listener is executed should set flow to falue and value Pair of declared shot to null when isSuccessful returns back false`() =
            runTest {
                val declaredShot = DeclaredShot(
                    id = 0,
                    shotCategory = "category",
                    title = "title",
                    description = "description"
                )
                val uid = "uid"
                val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}"

                val mockTaskVoidResult = mockk<Task<Void>>()
                val mockFirebaseUser = mockk<FirebaseUser>()
                val onCompleteListenerSlot = slot<OnCompleteListener<Void>>()
                val failureListenerSlot = slot<OnFailureListener>()

                val values = hashMapOf<String, Any>()

                values[Constants.ID] = declaredShot.id
                values[Constants.SHOT_CATEGORY] = declaredShot.shotCategory
                values[Constants.TITLE] = declaredShot.title
                values[Constants.DESCRIPTION] = declaredShot.description

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockTaskVoidResult.isSuccessful } returns false

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every { firebaseDatabase.getReference(path).key } returns key

                every {
                    firebaseDatabase.getReference(path).setValue(values)
                        .addOnCompleteListener(capture(onCompleteListenerSlot))
                        .addOnFailureListener(capture(failureListenerSlot))
                } answers {
                    onCompleteListenerSlot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(path).push()

                val key = "mockKey"
                every { reference.push().key } returns key

                every { reference.child(key).setValue(values, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(
                        declaredShot = declaredShot
                    ).first()

                Assertions.assertEquals(Pair(false, null), value)
            }

        @Test
        fun `when add on failure listener is executed should set flow to false and null Pair`() = runTest {
            val declaredShot = DeclaredShot(
                id = 0,
                shotCategory = "category",
                title = "title",
                description = "description"
            )
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}"

            val mockException = Exception("Simulated failure")

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Any>()

            values[Constants.ID] = declaredShot.id
            values[Constants.SHOT_CATEGORY] = declaredShot.shotCategory
            values[Constants.TITLE] = declaredShot.title
            values[Constants.DESCRIPTION] = declaredShot.description

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val reference = firebaseDatabase.getReference(path).push()

            val key = "mockKey"
            every { reference.push().key } returns key

            every { reference.child(key).setValue(values, any()) }

            val value =
                createFirebaseUserInfoImpl.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(
                    declaredShot = declaredShot
                ).first()

            Assertions.assertEquals(Pair(false, null), value)
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
                val onCompleteListenerSlot = slot<OnCompleteListener<Void>>()
                val failureListenerSlot = slot<OnFailureListener>()

                val values = hashMapOf<String, String>()

                values[Constants.USERNAME] = createAccountResult.username
                values[Constants.EMAIL] = createAccountResult.email

                mockkStatic(Tasks::class)
                mockkStatic(FirebaseUser::class)

                every { mockTaskVoidResult.isSuccessful } returns true

                every { mockFirebaseUser.uid } returns uid
                every { firebaseAuth.currentUser } returns mockFirebaseUser

                every { firebaseDatabase.getReference(path).key } returns key

                every {
                    firebaseDatabase.getReference(path).setValue(values)
                        .addOnCompleteListener(capture(onCompleteListenerSlot))
                        .addOnFailureListener(capture(failureListenerSlot))
                } answers {
                    onCompleteListenerSlot.captured.onComplete(mockTaskVoidResult)
                    mockTaskVoidResult
                }

                val reference = firebaseDatabase.getReference(path).push()

                every { reference.setValue(values, any()) }

                val value =
                    createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                        userName = createAccountResult.username,
                        email = createAccountResult.email
                    ).first()

                Assertions.assertEquals(Pair(true, key), value)
            }

        @Test
        fun `when add on failure listener is executed should set flow to false and null Pair`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid"

            val mockException = Exception("Simulated failure")

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, String>()
            values[Constants.USERNAME] = createAccountResult.username
            values[Constants.EMAIL] = createAccountResult.email

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val reference = firebaseDatabase.getReference(path).push()

            every { reference.setValue(values, any()) }

            val value = createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
                userName = createAccountResult.username,
                email = createAccountResult.email
            ).first()

            Assertions.assertEquals(Pair(false, null), value)
        }
    }

    @Nested
    inner class AttemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow {

        @Test
        fun `when add on failure listener is executed should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}"

            val mockException = Exception("Simulated failure")

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Any>()

            val reference = firebaseDatabase.getReference(path)

            values[Constants.FIRST_NAME] = playerInfoRealtimeResponse.firstName
            values[Constants.LAST_NAME] = playerInfoRealtimeResponse.lastName
            values[Constants.POSITION_VALUE] = playerInfoRealtimeResponse.positionValue
            values[Constants.IMAGE_URL] = playerInfoRealtimeResponse.imageUrl
            values[Constants.SHOTS_LOGGED] = playerInfoRealtimeResponse.shotsLogged

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            val key = "mockKey"
            every { reference.push().key } returns key

            every { mockTaskVoidResult.isSuccessful } returns false

            every {
                reference.child(key).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(playerInfoRealtimeResponse).first()

            Assertions.assertEquals(Pair(false, null), value)
        }

        @Test
        fun `when add on complete listener is executed should set flow to true when isSuccessful returns back true`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Any>()

            val reference = firebaseDatabase.getReference(path)

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

            every { reference.push().key } returns key

            every {
                reference.child(key).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                completeListenerSlot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(playerInfoRealtimeResponse = playerInfoRealtimeResponse).first()

            Assertions.assertEquals(Pair(true, key), value)
        }
    }

    @Nested
    inner class AttemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow {

        @Test
        fun `when add on complete listener is executed should set flow to true when isSuccessful returns back true`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Any>()

            val reference = firebaseDatabase.getReference(path)

            values[Constants.LOGGED_DATE_VALUE] = individualPlayerReportRealtimeResponse.loggedDateValue
            values[Constants.PLAYER_NAME] = individualPlayerReportRealtimeResponse.playerName
            values[Constants.PDF_URL] = individualPlayerReportRealtimeResponse.pdfUrl

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every { mockTaskVoidResult.isSuccessful } returns true

            every { reference.push().key } returns key

            every {
                reference.child(key).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                completeListenerSlot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(individualPlayerReportRealtimeResponse = individualPlayerReportRealtimeResponse).first()

            Assertions.assertEquals(Pair(true, key), value)
        }

        @Test
        fun `when add on complete listener is executed should set flow to true when isSuccessful returns back false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Any>()

            val reference = firebaseDatabase.getReference(path)

            values[Constants.LOGGED_DATE_VALUE] = individualPlayerReportRealtimeResponse.loggedDateValue
            values[Constants.PLAYER_NAME] = individualPlayerReportRealtimeResponse.playerName
            values[Constants.PDF_URL] = individualPlayerReportRealtimeResponse.pdfUrl

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every { mockTaskVoidResult.isSuccessful } returns false

            every { reference.push().key } returns key

            every {
                reference.child(key).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                completeListenerSlot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(individualPlayerReportRealtimeResponse = individualPlayerReportRealtimeResponse).first()

            Assertions.assertEquals(Pair(false, null), value)
        }

        @Test
        fun `when add on failure listener is executed should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.PLAYERS_INDIVIDUAL_REPORTS}"

            val mockException = Exception("Simulated failure")

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val completeListenerSlot = slot<OnCompleteListener<Void>>()
            val failureListenerSlot = slot<OnFailureListener>()

            val values = hashMapOf<String, Any>()

            val reference = firebaseDatabase.getReference(path)

            values[Constants.LOGGED_DATE_VALUE] = individualPlayerReportRealtimeResponse.loggedDateValue
            values[Constants.PLAYER_NAME] = individualPlayerReportRealtimeResponse.playerName
            values[Constants.PDF_URL] = individualPlayerReportRealtimeResponse.pdfUrl

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            val key = "mockKey"
            every { reference.push().key } returns key

            every {
                reference.child(key).setValue(values)
                    .addOnCompleteListener(capture(completeListenerSlot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val value = createFirebaseUserInfoImpl.attemptToCreateIndividualPlayerReportFirebaseRealtimeDatabaseResponseFlow(individualPlayerReportRealtimeResponse)
                .first()

            Assertions.assertEquals(Pair(false, null), value)
        }
    }
}
