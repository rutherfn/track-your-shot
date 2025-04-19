package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestDeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeWithKeyResponse
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

class UpdateFirebaseUserInfoImplTest {

    private lateinit var updateFirebaseUserInfoImpl: UpdateFirebaseUserInfoImpl

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    val playerInfoRealtimeWithKeyResponse = TestPlayerInfoRealtimeWithKeyResponse().create()

    val declaredShotWithKeyRealtimeResponse = TestDeclaredShotWithKeyRealtimeResponse.create()

    @BeforeEach
    fun beforeEach() {
        updateFirebaseUserInfoImpl = UpdateFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class UpdateDeclaredShot {

        @Test
        fun `when on complete listener is executed and returns isSuccessful returns true should set flow to true`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}/${declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val failureListenerSlot = slot<OnFailureListener>()
            val slot = slot<OnCompleteListener<Void>>()

            val declaredShotDataToUpdate =
                mapOf(
                    Constants.ID to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                    Constants.SHOT_CATEGORY to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                    Constants.TITLE to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                    Constants.DESCRIPTION to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description
                )

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .updateChildren(declaredShotDataToUpdate)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updateDeclaredShot(declaredShotWithKeyRealtimeResponse = declaredShotWithKeyRealtimeResponse).first()

            Assertions.assertEquals(true, value)
        }

        @Test
        fun `when on complete listener is executed and returns isSuccessful returns false should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}/${declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val failureListenerSlot = slot<OnFailureListener>()
            val slot = slot<OnCompleteListener<Void>>()

            val declaredShotDataToUpdate =
                mapOf(
                    Constants.ID to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                    Constants.SHOT_CATEGORY to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                    Constants.TITLE to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                    Constants.DESCRIPTION to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description
                )

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockTaskVoidResult.isSuccessful } returns false

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .updateChildren(declaredShotDataToUpdate)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updateDeclaredShot(declaredShotWithKeyRealtimeResponse = declaredShotWithKeyRealtimeResponse).first()

            Assertions.assertEquals(false, value)
        }

        @Test
        fun `when on failure listener is invoked should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS_PATH}/$uid/${Constants.CREATED_SHOTS}/${declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val failureListenerSlot = slot<OnFailureListener>()
            val slot = slot<OnCompleteListener<Void>>()

            val mockException = Exception("Simulated failure")

            val declaredShotDataToUpdate =
                mapOf(
                    Constants.ID to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                    Constants.SHOT_CATEGORY to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                    Constants.TITLE to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                    Constants.DESCRIPTION to declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description
                )

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .updateChildren(declaredShotDataToUpdate)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updateDeclaredShot(declaredShotWithKeyRealtimeResponse = declaredShotWithKeyRealtimeResponse).first()

            Assertions.assertEquals(false, value)
        }
    }

    @Nested
    inner class UpdatePlayer {

        @Test
        fun `when on complete listener is executed and returns isSuccessful returns true should set flow to true`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/${playerInfoRealtimeWithKeyResponse.playerFirebaseKey}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val failureListenerSlot = slot<OnFailureListener>()
            val slot = slot<OnCompleteListener<Void>>()

            val playerDataToUpdate = mapOf(
                Constants.FIRST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
                Constants.LAST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
                Constants.IMAGE_URL to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
                Constants.POSITION_VALUE to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue,
                Constants.SHOTS_LOGGED to playerInfoRealtimeWithKeyResponse.playerInfo.shotsLogged
            )

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .updateChildren(playerDataToUpdate)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updatePlayer(playerInfoRealtimeWithKeyResponse = playerInfoRealtimeWithKeyResponse).first()

            Assertions.assertEquals(true, value)
        }

        @Test
        fun `when on complete listener is executed and returns isSuccessful returns false should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/${playerInfoRealtimeWithKeyResponse.playerFirebaseKey}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val failureListenerSlot = slot<OnFailureListener>()
            val slot = slot<OnCompleteListener<Void>>()

            val playerDataToUpdate = mapOf(
                Constants.FIRST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
                Constants.LAST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
                Constants.IMAGE_URL to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
                Constants.POSITION_VALUE to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue,
                Constants.SHOTS_LOGGED to playerInfoRealtimeWithKeyResponse.playerInfo.shotsLogged
            )

            mockkStatic(Tasks::class)
            mockkStatic(FirebaseUser::class)

            every { mockTaskVoidResult.isSuccessful } returns false

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .updateChildren(playerDataToUpdate)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updatePlayer(playerInfoRealtimeWithKeyResponse = playerInfoRealtimeWithKeyResponse).first()

            Assertions.assertEquals(false, value)
        }

        @Test
        fun `when on failure listener is invoked should set flow to false`() = runTest {
            val uid = "uid"
            val path = "${Constants.USERS}/$uid/${Constants.PLAYERS}/${playerInfoRealtimeWithKeyResponse.playerFirebaseKey}"

            val mockTaskVoidResult = mockk<Task<Void>>()
            val mockFirebaseUser = mockk<FirebaseUser>()
            val failureListenerSlot = slot<OnFailureListener>()
            val slot = slot<OnCompleteListener<Void>>()

            val mockException = Exception("Simulated failure")

            val playerDataToUpdate = mapOf(
                Constants.FIRST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
                Constants.LAST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
                Constants.IMAGE_URL to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
                Constants.POSITION_VALUE to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue,
                Constants.SHOTS_LOGGED to playerInfoRealtimeWithKeyResponse.playerInfo.shotsLogged
            )

            every { mockFirebaseUser.uid } returns uid
            every { firebaseAuth.currentUser } returns mockFirebaseUser

            every {
                firebaseDatabase.getReference(path)
                    .updateChildren(playerDataToUpdate)
                    .addOnCompleteListener(capture(slot))
                    .addOnFailureListener(capture(failureListenerSlot))
            } answers {
                failureListenerSlot.captured.onFailure(mockException)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updatePlayer(playerInfoRealtimeWithKeyResponse = playerInfoRealtimeWithKeyResponse).first()

            Assertions.assertEquals(false, value)
        }
    }
}
