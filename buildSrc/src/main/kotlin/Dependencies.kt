
/**
 * To define dependencies
 */
object Dependencies {

    object Android {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Dependencies.Android.appCompat}"
        const val ktx = "androidx.core:core-ktx:${Versions.Dependencies.Android.ktx}"
        const val testRules = "androidx.test:rules:${Versions.Dependencies.Android.testRules}"
    }

    object AppCenter {
        const val analytics = "com.microsoft.appcenter:appcenter-analytics:${Versions.Dependencies.AppCenter.core}"
        const val crashes = "com.microsoft.appcenter:appcenter-crashes:${Versions.Dependencies.AppCenter.core}"
    }

    object Accompanist {
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.Dependencies.SystemUiController.core}"
    }

    object Coil {
        const val core = "io.coil-kt:coil:${Versions.Dependencies.Coil.core}"
    }

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Versions.Dependencies.Compose.activity}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.Dependencies.Compose.core}"
        const val material = "androidx.compose.material:material:${Versions.Dependencies.Compose.core}"
        const val materialDesignIconsCore = "androidx.compose.material:material-icons-core:${Versions.Dependencies.Compose.core}"
        const val materialDesignIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Dependencies.Compose.core}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.Dependencies.Compose.navigation}"
        const val tooling = "androidx.compose.ui:ui-tooling:${Versions.Dependencies.Compose.core}"
        const val ui = "androidx.compose.ui:ui:${Versions.Dependencies.Compose.core}"
        const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Versions.Dependencies.Compose.uiTest}"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.Dependencies.Compose.uiTest}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Dependencies.Compose.viewModel}"
    }

    object Coroutine {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Dependencies.Coroutine.core}"
        const val jvm = "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Versions.Dependencies.Coroutine.jvm}"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Dependencies.Coroutine.jvm}"
    }

    object Espresso {
        const val core = "androidx.test.espresso:espresso-core:${Versions.Dependencies.Espresso.core}"
        const val idilingResource = "androidx.test.espresso:espresso-idling-resource:${Versions.Dependencies.Espresso.core}"
    }

    object Firebase {
        const val authKtx = "com.google.firebase:firebase-auth-ktx:${Versions.Dependencies.Firebase.authKtx}"
        const val bom = "com.google.firebase:firebase-bom:${Versions.Dependencies.Firebase.bom}"
        const val databaseKtx = "com.google.firebase:firebase-database-ktx:${Versions.Dependencies.Firebase.ktx}"
    }

    object Junit {
        const val ext = "androidx.test.ext:junit:${Versions.Dependencies.JunitJupiter.ext}"
        const val junit = "de.mannodermaus.gradle.plugins:android-junit5:${Versions.Dependencies.JunitJupiter.junit}"

        object Jupiter {
            const val api = "org.junit.jupiter:junit-jupiter-api:${Versions.Dependencies.JunitJupiter.junitJupiter}"
            const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.Dependencies.JunitJupiter.junitJupiter}"
            const val params = "org.junit.jupiter:junit-jupiter-params:${Versions.Dependencies.JunitJupiter.junitJupiter}"
        }
    }

    object Koin {
        const val compose = "io.insert-koin:koin-androidx-compose:${Versions.Dependencies.Koin.latestVersion}"
        const val core = "io.insert-koin:koin-android:${Versions.Dependencies.Koin.latestVersion}"
        const val koinTest = "io.insert-koin:koin-test:${Versions.Dependencies.Koin.latestVersion}"
        const val test = "io.insert-koin:koin-core:${Versions.Dependencies.Koin.latestVersion}"
        const val testJunit4 = "io.insert-koin:koin-test-junit4:${Versions.Dependencies.Koin.latestVersion}"
    }

    object Lifecycle {
        const val compose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Dependencies.Lifecycle.core}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Dependencies.Lifecycle.core}"
    }

    object Material {
        const val material = "com.google.android.material:material:${Versions.Dependencies.Material.material}"
    }

    object Mockk {
        const val core = "io.mockk:mockk:${Versions.Dependencies.Mockk.core}"
    }

    object Room {
        const val compiler = "androidx.room:room-runtime:${Versions.Dependencies.Room.core}"
        const val kaptCompiler = "androidx.room:room-compiler:${Versions.Dependencies.Room.core}"
        const val runtime = "androidx.room:room-runtime:${Versions.Dependencies.Room.core}"
    }

    object Timber {
        const val core = "com.jakewharton.timber:timber:${Versions.Dependencies.Timber.core}"
    }
}
