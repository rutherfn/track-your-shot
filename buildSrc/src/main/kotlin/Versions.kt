object Versions {

    /**
     * To define plugins versions */
    object Plugins {
        const val gradlePlugin = "7.2.0"
        const val kotlin = "1.7.10"
    }

    /**
     *  To define dependencies versions */
    object Dependencies {

        object Android {
            const val appCompat = "1.4.1"
            const val ktx = "1.7.0"
            const val testRules = "1.5.0"
        }

        object AppCenter {
            const val core = "4.3.1"
        }

        object Coil { const val core = "2.2.2" }

        object Compose {
            const val activity = "1.5.1"
            const val core = "1.2.1"
            const val navigation = "2.5.3"
            const val viewModel = "1.5.1"
            const val uiTest = "1.3.0"
        }

        object CoreTesting {
            const val core = "2.2.0"
        }

        object Coroutine {
            const val core = "1.3.9"
            const val jvm = "1.6.4"
        }

        object Espresso { const val core = "3.3.0" }

        object Firebase {
            const val authKtx = "21.1.0"
            const val bom = "31.0.3"
            const val ktx = "20.1.0"
        }

        object Junit {
            const val core = "4.13.2"
            const val textExt = ""
        }

        object JunitJupiter {
            const val ext = "1.1.3"
            const val junit = "1.8.2.1"
            const val junitJupiter = "5.7.2"
        }

        object Koin { const val latestVersion = "3.2.0" }

        object KtLint { const val ktLint = "11.0.0" }

        object Lifecycle {
            const val core = "2.6.0-alpha02"
            const val runtimeKtx = "2.4.1"
        }

        object Material { const val material = "1.5.0" }

        object Mockk { const val core = "1.13.2" }

        object Room { const val core = "2.4.1" }
        object SystemUiController { const val core = "0.27.0" }

        object Timber { const val core = "5.0.1" }

        object Truth { const val core = "1.0.1" }
    }
}
