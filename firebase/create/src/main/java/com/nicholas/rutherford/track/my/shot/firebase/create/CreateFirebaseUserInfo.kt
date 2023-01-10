package com.nicholas.rutherford.track.my.shot.firebase.create

import com.nicholas.rutherford.track.my.shot.account.info.CreateAccountResponse
import kotlinx.coroutines.flow.Flow

interface CreateFirebaseUserInfo {
    fun attemptToCreateAccountResponseFlow(email: String, password: String): Flow<CreateAccountResponse>
}
