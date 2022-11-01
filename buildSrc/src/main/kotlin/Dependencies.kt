
/**
 * To define dependencies
 */
object Dependencies {

    object Android {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.Dependencies.Android.appCompat}"
        const val ktx = "androidx.core:core-ktx:${Versions.Dependencies.Android.ktx}"
    }

    object Compose {
        val activity = "androidx.activity:activity-compose:${Versions.Dependencies.Compose.activity}"
        val foundation = "androidx.compose.foundation:foundation:${Versions.Dependencies.Compose.core}"
        val material = "androidx.compose.material:material:${Versions.Dependencies.Compose.core}"
        val materialDesignIconsCore = "androidx.compose.material:material-icons-core:${Versions.Dependencies.Compose.core}"
        val materialDesignIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Dependencies.Compose.core}"
        val navigation = "androidx.navigation:navigation-compose:${Versions.Dependencies.Compose.navigation}"
        val tooling = "androidx.compose.ui:ui-tooling:${Versions.Dependencies.Compose.core}"
        val ui = "androidx.compose.ui:ui:${Versions.Dependencies.Compose.core}"
        val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Versions.Dependencies.Compose.uiTest}"
        val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.Dependencies.Compose.uiTest}"
        val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Dependencies.Compose.viewModel}"
    }

    object Espresso {
        val core = "androidx.test.espresso:espresso-core:${Versions.Dependencies.Espresso.core}"
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
        val core = "io.insert-koin:koin-android:${Versions.Dependencies.Koin.latestVersion}"
        val koinTest = "io.insert-koin:koin-test:${Versions.Dependencies.Koin.latestVersion}"
        val test = "io.insert-koin:koin-core:${Versions.Dependencies.Koin.latestVersion}"
        val testJunit4 = "io.insert-koin:koin-test-junit4:${Versions.Dependencies.Koin.latestVersion}"
    }

    object Material {
        const val material = "com.google.android.material:material:${Versions.Dependencies.Material.material}"
    }
}
