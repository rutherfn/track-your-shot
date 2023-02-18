package com.nicholas.rutherford.track.my.shot.firebase.util.existinguser

import com.google.firebase.auth.FirebaseAuth

class ExistingUserFirebaseImpl(private val firebaseAuth: FirebaseAuth) : ExistingUserFirebase {

    override fun logOut() = firebaseAuth.signOut()
}
