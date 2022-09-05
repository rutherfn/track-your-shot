
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
        val foundation = "androidx.compose.foundation:foundation:${Versions.Dependencies.Compose.compose}"
        val material = "androidx.compose.material:material:${Versions.Dependencies.Compose.compose}"
        val materialDesignIconsCore = "androidx.compose.material:material-icons-core:${Versions.Dependencies.Compose.compose}"
        val materialDesignIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Dependencies.Compose.compose}"
        val tooling = "androidx.compose.ui:ui-tooling:${Versions.Dependencies.Compose.compose}"
        val ui = "androidx.compose.ui:ui:${Versions.Dependencies.Compose.compose}"
        val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Dependencies.Compose.compose}"
    }

    object Junit {
        const val junit = "de.mannodermaus.gradle.plugins:android-junit5:${Versions.Dependencies.JunitJupiter.junit}"

        object Jupiter {
            const val api = "org.junit.jupiter:junit-jupiter-api:${Versions.Dependencies.JunitJupiter.junitJupiter}"
            const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.Dependencies.JunitJupiter.junitJupiter}"
            const val params = "org.junit.jupiter:junit-jupiter-params:${Versions.Dependencies.JunitJupiter.junitJupiter}"
        }
    }

    object KLint {
        const val klint = "org.jlleitschuh.gradle.ktlint"
    }

    object Material {
        const val material = "com.google.android.material:material:${Versions.Dependencies.Material.material}"
    }
}
