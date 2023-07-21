plugins {
    id(BuildIds.androidLibrary)
    id("kotlin-kapt")
    kotlin(BuildIds.pluginKotlin)
    kotlin(BuildIds.pluginKapt)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
}

kapt {
    correctErrorTypes = true
}

android {
    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    compileOptions {
        sourceCompatibility = types.BuildTypes.CompileOptions.sourceCompatibility
        targetCompatibility = types.BuildTypes.CompileOptions.targetCompatibility
    }

    defaultConfig {
        minSdk = ConfigurationData.minSdk
        targetSdk = ConfigurationData.targetSdk

        testInstrumentationRunner = ConfigurationData.testInstrumentationRunner
    }

    buildTypes {
        getByName(types.BuildTypes.UniqueBuilds.Release.buildName) {
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Release.isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile(types.BuildTypes.proguardAndroidOptimizeTxt),
                types.BuildTypes.proguardRulesPro
            )
        }

        getByName(types.BuildTypes.UniqueBuilds.Debug.buildName) {
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Debug.isMinifyEnabled
        }

        create(types.BuildTypes.UniqueBuilds.Stage.buildName) {
            initWith(getByName(types.BuildTypes.UniqueBuilds.Debug.buildName))
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Stage.isMinifyEnabled
        }
    }
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java", "src/main/test")
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = KotlinOptions.jvmTarget
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.getByPath(TaskOptions.preBuildPath).dependsOn(TaskOptions.ktlintFormatPath)
}

dependencies {
    androidTestImplementation(Dependencies.Junit.ext)
    androidTestImplementation(Dependencies.CoreTesting.core)
    androidTestImplementation(Dependencies.Espresso.core)
    androidTestImplementation(Dependencies.Truth.core)

    implementation(Dependencies.Room.ktx)
    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.coroutines)
    kapt(Dependencies.Room.compiler)

    testImplementation(Dependencies.Junit.core)

    androidTestImplementation(project(mapOf("path" to ":data-test:room")))
}
