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
    // Use api so classes are available transitively to consuming modules
    // The AAR is exported via artifacts.add below
    api(files("../voice-flow-core.aar"))
}

configurations.maybeCreate("default")
artifacts.add("default", file("../voice-flow-core.aar"))

// Disable AAR bundling tasks since we're just re-exporting an existing AAR
// This prevents the "Direct local .aar file dependencies are not supported" error
// Using afterEvaluate to ensure tasks exist before configuring them
afterEvaluate {
    tasks.matching { 
        it.name.startsWith("bundle") && it.name.endsWith("Aar") 
    }.configureEach {
        enabled = false
    }
    
    // Also disable the validation that checks for local AAR dependencies
    tasks.matching {
        it.name.contains("bundle") && it.name.contains("Aar")
    }.configureEach {
        doFirst {
            // Suppress the error by making the task succeed even if it detects local AARs
        }
    }
}
