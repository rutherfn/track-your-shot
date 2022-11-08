
/**
 * To define dependencies
 */
object Dependencies {

    object Android {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Dependencies.Android.appCompat}"
        const val ktx = "androidx.core:core-ktx:${Versions.Dependencies.Android.ktx}"
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
        const val core = "io.insert-koin:koin-android:${Versions.Dependencies.Koin.latestVersion}"
        const val koinTest = "io.insert-koin:koin-test:${Versions.Dependencies.Koin.latestVersion}"
        const val test = "io.insert-koin:koin-core:${Versions.Dependencies.Koin.latestVersion}"
        const val testJunit4 = "io.insert-koin:koin-test-junit4:${Versions.Dependencies.Koin.latestVersion}"
    }

    object Material {
        const val material = "com.google.android.material:material:${Versions.Dependencies.Material.material}"
    }
}
