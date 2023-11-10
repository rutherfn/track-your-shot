package com.nicholas.rutherford.track.your.shot.firebase.core.create

import kotlinx.coroutines.flow.Flow
import java.util.Date

interface CreateFirebaseLastUpdated {
    fun attemptToCreateLastUpdatedFlow(date: Date): Flow<Boolean>
}
