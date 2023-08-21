package com.nicholas.rutherford.track.my.shot.firebase.create

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface CreateFirebaseLastUpdated {
    fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean>
}
