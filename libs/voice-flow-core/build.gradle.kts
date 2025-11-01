import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(BuildIds.androidLibrary)
    kotlin(BuildIds.pluginKotlin)
}

android {
    namespace = "com.nicholas.rutherford.track.your.shot.libs.voiceflow"

    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    compileOptions {
        sourceCompatibility = types.BuildTypes.CompileOptions.sourceCompatibility
        targetCompatibility = types.BuildTypes.CompileOptions.targetCompatibility
    }

    defaultConfig {
        minSdk = ConfigurationData.minSdk
    }

    buildTypes {
        getByName(types.BuildTypes.UniqueBuilds.Release.buildName) {
            isMinifyEnabled = false
        }
        getByName(types.BuildTypes.UniqueBuilds.Debug.buildName) {
            isMinifyEnabled = false
        }
        create(types.BuildTypes.UniqueBuilds.Stage.buildName) {
            initWith(getByName(types.BuildTypes.UniqueBuilds.Debug.buildName))
            isMinifyEnabled = false
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(KotlinOptions.jvmTarget))
        }
    }
}

dependencies {
    // Add the AAR to the classpath so classes are available for compilation
    api(files("../voice-flow-core.aar"))
}

configurations.maybeCreate("default")
artifacts.add("default", file("../voice-flow-core.aar"))
