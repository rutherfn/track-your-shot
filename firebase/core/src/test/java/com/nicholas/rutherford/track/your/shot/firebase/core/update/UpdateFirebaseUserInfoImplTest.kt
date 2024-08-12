package com.nicholas.rutherford.track.your.shot.firebase.core.update

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    val firebaseAccountKey = "-kTE1212D"
    val playerInfoRealtimeWithKeyResponse = TestPlayerInfoRealtimeWithKeyResponse().create()

    @BeforeEach
    fun beforeEach() {
        updateFirebaseUserInfoImpl = UpdateFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class UpdatePlayer {

        @Test
        fun `when on complete listener is executed and returns isSuccessful returns true should set flow to true`() = runTest {
            val mockTaskVoidResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()
            val playerDataToUpdate = mapOf(
                Constants.FIRST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.firstName,
                Constants.LAST_NAME to playerInfoRealtimeWithKeyResponse.playerInfo.lastName,
                Constants.IMAGE_URL to playerInfoRealtimeWithKeyResponse.playerInfo.imageUrl,
                Constants.POSITION_VALUE to playerInfoRealtimeWithKeyResponse.playerInfo.positionValue,
                Constants.SHOTS_LOGGED to playerInfoRealtimeWithKeyResponse.playerInfo.shotsLogged
            )

            mockkStatic(Tasks::class)

            every { mockTaskVoidResult.isSuccessful } returns true

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .child(playerInfoRealtimeWithKeyResponse.playerFirebaseKey)
                    .updateChildren(playerDataToUpdate)
                    .addOnCompleteListener(capture(slot))
            } answers {
                slot.captured.onComplete(mockTaskVoidResult)
                mockTaskVoidResult
            }

            val value = updateFirebaseUserInfoImpl.updatePlayer(
                playerInfoRealtimeWithKeyResponse = playerInfoRealtimeWithKeyResponse
            ).first()

            Assertions.assertEquals(true, value)
        }
    }
}
