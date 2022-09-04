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

        object Compose {
            const val activity = "1.5.1"
            const val compose = "1.2.1"
            const val viewModel = "1.5.1"
        }

        object JunitJupiter { const val junitJupiter = "5.7.2" }

        object Material { const val material = "1.5.0" }
    }
}
