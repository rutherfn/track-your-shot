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
        }

        object AppCenter {
            const val core = "4.3.1"
        }

        object Auth0 {
            const val core = "2.+"
        }

        object Coil { const val core = "2.2.2" }

        object Compose {
            const val activity = "1.5.1"
            const val core = "1.2.1"
            const val navigation = "2.5.3"
            const val viewModel = "1.5.1"
            const val uiTest = "1.3.0"
        }

        object Coroutine {
            const val core = "1.3.9"
            const val jvm = "1.6.4"
        }

        object Espresso {
            const val core = "3.4.0"
        }

        object Firebase {
            const val bom = "31.0.3"
        }

        object JunitJupiter {
            const val ext = "1.1.3"
            const val junit = "1.8.2.1"
            const val junitJupiter = "5.7.2"
        }

        object Koin { const val latestVersion = "3.2.0" }

        object KtLint { const val ktLint = "11.0.0" }

        object Lifecycle { const val core = "2.6.0-alpha02" }

        object Material { const val material = "1.5.0" }

        object Mockk { const val core = "1.13.2" }

        object Room { const val core = "2.4.4" }
    }
}
