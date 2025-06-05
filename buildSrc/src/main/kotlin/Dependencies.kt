
/**
 * To define dependencies
 */
object Dependencies {

    object Android {
        const val annotationJvm = "androidx.annotation:annotation-jvm:${Versions.Dependencies.Android.annotationJvm}"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Dependencies.Android.appCompat}"
        const val core = "androidx.core:core:"
        const val ktx = "androidx.core:core-ktx:${Versions.Dependencies.Android.ktx}"
        const val testRules = "androidx.test:rules:${Versions.Dependencies.Android.testRules}"
    }

    object Accompanist {
        const val permissions = "com.google.accompanist:accompanist-permissions:${Versions.Dependencies.Accompanist.permissions}"
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.Dependencies.Accompanist.systemUiController}"
    }

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Versions.Dependencies.Compose.activity}"
        const val bom = "androidx.compose:compose-bom:${Versions.Dependencies.Compose.bom}"
        const val coil = "io.coil-kt:coil-compose:${Versions.Dependencies.Compose.coil}"
        const val foundation = "androidx.compose.foundation:foundation:${Versions.Dependencies.Compose.core}"
        const val ktx = "androidx.activity:activity-ktx:${Versions.Dependencies.Compose.activity}"
        const val material = "androidx.compose.material:material:${Versions.Dependencies.Compose.core}"
        const val materialDesignIconsCore = "androidx.compose.material:material-icons-core:${Versions.Dependencies.Compose.core}"
        const val materialDesignIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Dependencies.Compose.core}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.Dependencies.Compose.navigation}"
        const val ui = "androidx.compose.ui:ui:${Versions.Dependencies.Compose.core}"
        const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Versions.Dependencies.Compose.uiTest}"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.Dependencies.Compose.uiTest}"
        const val uiTooling = "androidx.compose.ui:ui-tooling"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Dependencies.Compose.viewModel}"
    }

    object CoreTesting {
        const val core = "androidx.arch.core:core-testing:${Versions.Dependencies.CoreTesting.core}"
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
        const val analytics = "com.google.firebase:firebase-analytics:${Versions.Dependencies.Firebase.analytics}"
        const val authKtx = "com.google.firebase:firebase-auth-ktx:${Versions.Dependencies.Firebase.authKtx}"
        const val bom = "com.google.firebase:firebase-bom:${Versions.Dependencies.Firebase.bom}"
        const val databaseKtx = "com.google.firebase:firebase-database-ktx:${Versions.Dependencies.Firebase.ktx}"
        const val storageKtx = "com.google.firebase:firebase-storage-ktx:${Versions.Dependencies.Firebase.ktx}"
    }

    object Gson {
        const val core = "com.google.code.gson:gson:${Versions.Dependencies.Gson.core}"
    }

    object Junit {
        const val core = "junit:junit:${Versions.Dependencies.Junit.core}"
        const val ext = "androidx.test.ext:junit:${Versions.Dependencies.JunitJupiter.ext}"
        const val junit = "de.mannodermaus.gradle.plugins:android-junit5:${Versions.Dependencies.JunitJupiter.junit}"
    }
}
