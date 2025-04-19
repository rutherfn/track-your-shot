package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity

object TestShotIgnoringEntity {

    fun build(): ShotIgnoringEntity {
        return ShotIgnoringEntity(
            id = SHOT_IGNORING_ID,
            shotId = SHOT_IGNORING_SHOT_ID
        )
    }
}

const val SHOT_IGNORING_ID = 1
const val SHOT_IGNORING_SHOT_ID = 22

// @Test
// fun `when add on complete listener is executed should set flow to true and value Pair when isSuccessful returns back true`() =
//    runTest {
//        val uid = "uid"
//        val path = "${Constants.USERS_PATH}/$uid"
//
//        val mockTaskVoidResult = mockk<Task<Void>>()
//        val mockFirebaseUser = mockk<FirebaseUser>()
//        val onCompleteListenerSlot = slot<OnCompleteListener<Void>>()
//        val failureListenerSlot = slot<OnFailureListener>()
//
//        val values = hashMapOf<String, String>()
//
//        values[Constants.USERNAME] = createAccountResult.username
//        values[Constants.EMAIL] = createAccountResult.email
//
//        mockkStatic(Tasks::class)
//        mockkStatic(FirebaseUser::class)
//
//        every { mockTaskVoidResult.isSuccessful } returns true
//
//        every { mockFirebaseUser.uid } returns uid
//        every { firebaseAuth.currentUser } returns mockFirebaseUser
//
//        every { firebaseDatabase.getReference(path).key } returns key
//
//        every {
//            firebaseDatabase.getReference(path).setValue(values)
//                .addOnCompleteListener(capture(onCompleteListenerSlot))
//                .addOnFailureListener(capture(failureListenerSlot))
//        } answers {
//            onCompleteListenerSlot.captured.onComplete(mockTaskVoidResult)
//            mockTaskVoidResult
//        }
//
//        val reference = firebaseDatabase.getReference(path).push()
//
//        every { reference.setValue(values, any()) }
//
//        val value =
//            createFirebaseUserInfoImpl.attemptToCreateAccountFirebaseRealTimeDatabaseResponseFlow(
//                userName = createAccountResult.username,
//                email = createAccountResult.email
//            ).first()
//
//        Assertions.assertEquals(Pair(true, key), value)
//    }
