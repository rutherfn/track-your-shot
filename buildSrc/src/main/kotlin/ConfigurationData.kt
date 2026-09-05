
/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * To define configuration data */
object ConfigurationData {
    const val ktlintVersion = "11.5.0"
    // Android 16 (API 36). API 37 needs AGP 9.1.1+ / newer Android Studio than this machine allows.
    const val compileSdk = 36
    const val buildToolsVersion = "36.0.0"
    const val minSdk = 26
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    const val targetSdk = 36
    const val versionCode = 32
    const val versionName = "1.4"
}
