package types

import org.gradle.api.JavaVersion

/**
 * To define build types data */
object BuildTypes {
    const val proguardAndroidOptimizeTxt = "proguard-android-optimize.txt"
    const val proguardRulesPro = "proguard.rules.pro"

    object CompileOptions {
        val sourceCompatibility = JavaVersion.VERSION_1_8
        val targetCompatibility = JavaVersion.VERSION_1_8
    }

    object ManifiestOptions {
        const val appLabel = "appLabel"
        const val appIcon = "appIcon"
        const val roundAppIcon = "roundAppIcon"
    }

    object UniqueBuilds {
        object Debug : Build {
            override var appName: String = "Debug - TMS"
            override var applicationIdSuffix: String = ".debug"
            override var buildName: String = "debug"
            override var isDebuggable: Boolean = true
            override var isMinifyEnabled: Boolean = false
            override var appIconRoute: String = "@drawable/ic_launcher_test"
            override var roundAppIconRoute: String = "@drawable/ic_launcher_round_test"
        }

        object Release : Build {
            override var appName: String = "Track My Shot"
            override var applicationIdSuffix: String = ".release"
            override var buildName: String = "release"
            override var isDebuggable: Boolean = false
            override var isMinifyEnabled: Boolean = true
            override var appIconRoute: String = "@drawable/ic_launcher"
            override var roundAppIconRoute: String = "@drawable/ic_launcher_round"
        }

        object Stage : Build {
            override var appName: String = "Stage - TMS"
            override var applicationIdSuffix: String = ".stage"
            override var buildName: String = "stage"
            override var isDebuggable: Boolean = true
            override var isMinifyEnabled: Boolean = false
            override var appIconRoute: String = "@drawable/ic_launcher_stage"
            override var roundAppIconRoute: String = "@drawable/ic_launcher_round_stage"
        }
    }
}
