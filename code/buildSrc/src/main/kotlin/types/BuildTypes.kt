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

    object UniqueBuilds {
        object Debug : Build {
            override var buildName: String = "Debug"
            override var isMinifyEnabled: Boolean = false
        }

        object Release : Build {
            override var buildName: String = "release"
            override var isMinifyEnabled: Boolean = false
        }

        object Stage : Build {
            override var buildName: String = "Stage"
            override var isMinifyEnabled: Boolean = false
        }
    }
}
