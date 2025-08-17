package com.nicholas.rutherford.track.your.shot.koin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfoImpl
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebase
import com.nicholas.rutherford.track.your.shot.firebase.util.authentication.AuthenticationFirebaseImpl
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebase
import com.nicholas.rutherford.track.your.shot.firebase.util.existinguser.ExistingUserFirebaseImpl
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Koin module for providing Firebase-related dependencies.
 *
 * This module defines singletons for Firebase core services:
 * - [FirebaseAuth] for authentication.
 * - [FirebaseDatabase] for real-time database operations.
 * - [FirebaseStorage] for file storage.
 *
 * It also provides implementations for user management and authentication:
 * - [CreateFirebaseUserInfo] to create new user info in Firebase.
 * - [AuthenticationFirebase] for login/logout operations.
 * - [ExistingUserFirebase] to check if a user exists.
 * - [ReadFirebaseUserInfo] to read user info from Firebase.
 * - [UpdateFirebaseUserInfo] to update user info in Firebase.
 * - [DeleteFirebaseUserInfo] to delete user info from Firebase.
 */
object FirebaseModule {

    /** Koin module definitions. */
    val modules = module {

        /** Provides a singleton instance of [FirebaseAuth] for authentication. */
        single { FirebaseAuth.getInstance() }

        /** Provides a singleton instance of [FirebaseDatabase] for database operations. */
        single { FirebaseDatabase.getInstance() }

        /** Provides a singleton instance of [FirebaseStorage] for file storage. */
        single { FirebaseStorage.getInstance() }

        /**
         * Provides an implementation of [CreateFirebaseUserInfo] for creating user information
         * in Firebase using auth, storage, and database services.
         */
        single<CreateFirebaseUserInfo> {
            CreateFirebaseUserInfoImpl(
                firebaseAuth = get(),
                firebaseStorage = get(),
                firebaseDatabase = get()
            )
        }

        /**
         * Provides an implementation of [AuthenticationFirebase] for handling user authentication
         * operations using FirebaseAuth.
         */
        single<AuthenticationFirebase> {
            AuthenticationFirebaseImpl(firebaseAuth = get())
        }

        /**
         * Provides an implementation of [ExistingUserFirebase] to check if a user already exists
         * in Firebase.
         */
        single<ExistingUserFirebase> {
            ExistingUserFirebaseImpl(firebaseAuth = get())
        }

        /**
         * Provides an implementation of [ReadFirebaseUserInfo] for reading user information
         * from Firebase using FirebaseAuth and FirebaseDatabase.
         */
        single<ReadFirebaseUserInfo> {
            ReadFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
        }

        /**
         * Provides an implementation of [UpdateFirebaseUserInfo] for updating user information
         * in Firebase.
         */
        single<UpdateFirebaseUserInfo> {
            UpdateFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
        }

        /**
         * Provides an implementation of [DeleteFirebaseUserInfo] for deleting user information
         * from Firebase.
         */
        single<DeleteFirebaseUserInfo> {
            DeleteFirebaseUserInfoImpl(firebaseAuth = get(), firebaseDatabase = get())
        }
    }
}
