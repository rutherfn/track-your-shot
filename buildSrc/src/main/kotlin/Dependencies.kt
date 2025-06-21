
/**
 * To define dependencies
 */
object Dependencies {

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
}
