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
    
    // Completely disable lint for this wrapper module since it doesn't contain source code
    lint {
        disable.addAll(listOf("All"))
        checkReleaseBuilds = false
        checkAllWarnings = false
        ignoreWarnings = true
        abortOnError = false
        quiet = true
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
    api(files("../voice-flow-core.aar"))
}

configurations.maybeCreate("default")
artifacts.add("default", file("../voice-flow-core.aar"))

// Disable AAR bundling tasks - they cause "Direct local .aar file dependencies" error
// The artifact export above makes the AAR available to consuming modules without bundling
afterEvaluate {
    // Disable bundle tasks to prevent the validation error
    tasks.matching { 
        it.name.matches(Regex("bundle(Release|Debug|Stage)Aar"))
    }.configureEach {
        enabled = false
    }
    
    // Prepare AAR extraction directory for lint - handle all variants
    val sourceAarFile = file("../voice-flow-core.aar")
    val variants = listOf("release", "debug", "stage")
    
    // Ensure directory exists and AAR is available before any extraction tasks
    tasks.matching {
        it.name.contains("preBuild") || 
        it.name.contains("preReleaseBuild") || 
        it.name.contains("preDebugBuild") ||
        it.name.contains("preStageBuild")
    }.configureEach {
        doLast {
            // Prepare AAR for all variants
            variants.forEach { variant ->
                val extractDir = layout.buildDirectory.dir("intermediates/local_aar_for_lint/$variant").get().asFile
                val expectedAarFile = extractDir.resolve("out.aar")
                extractDir.mkdirs()
                if (sourceAarFile.exists() && !expectedAarFile.exists()) {
                    sourceAarFile.copyTo(expectedAarFile, overwrite = false)
                }
            }
        }
    }
    
    // Also ensure it's available for extraction transforms
    tasks.matching {
        it.name.contains("ExtractAar")
    }.configureEach {
        doFirst {
            // Prepare all variants to be safe
            variants.forEach { variant ->
                val extractDir = layout.buildDirectory.dir("intermediates/local_aar_for_lint/$variant").get().asFile
                val expectedAarFile = extractDir.resolve("out.aar")
                extractDir.mkdirs()
                if (sourceAarFile.exists() && !expectedAarFile.exists()) {
                    sourceAarFile.copyTo(expectedAarFile, overwrite = false)
                }
            }
        }
    }
    
    // Allow lint model generation but skip actual lint analysis
    // Other modules need the lint model metadata for their lint checks
    tasks.matching {
        (it.name.contains("lint") || it.name.contains("Lint")) && 
        !it.name.contains("Model") && 
        !it.name.contains("Metadata")
    }.configureEach {
        enabled = false
    }
}
