package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.firebase.auth.FirebaseAuth

class ReadFirebaseUserInfoImpl(firebaseAuth: FirebaseAuth) : ReadFirebaseUserInfo {

    override val isLoggedIn: Boolean = firebaseAuth.currentUser != null
}