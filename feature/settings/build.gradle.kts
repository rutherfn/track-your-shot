import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(BuildIds.androidLibrary)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version ConfigurationData.ktlintVersion
    id(BuildIds.kover)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.nicholas.rutherford.track.your.shot.feature.settings"

    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    buildFeatures {
        compose = ComposeData.Enabled.value
    }

    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = ComposeData.KotlinCompiler.extensionVersion
    }

    compileOptions {
        sourceCompatibility = types.BuildTypes.CompileOptions.sourceCompatibility
        targetCompatibility = types.BuildTypes.CompileOptions.targetCompatibility
    }

    defaultConfig {
        minSdk = ConfigurationData.minSdk

        testInstrumentationRunner = ConfigurationData.testInstrumentationRunner
    }

    testOptions {
        targetSdk = ConfigurationData.targetSdk
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
                srcDirs("src/main/java")
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(KotlinOptions.jvmTarget))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    api(project(path = ":base:vm"))
    api(project(path = ":base-resources"))
    api(project(path = ":build-type"))
    api(project(path = ":compose:components"))
    api(project(path = ":data-store"))
    api(project(path = ":firebase:core"))
    api(project(path = ":helper:account"))
    api(project(path = ":helper:extensions"))
    api(project(path = ":navigation"))

    debugImplementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.firebase.auth.ktx)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.android.junit5)

    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.jupiter.engine)

    testImplementation(project(path = ":data-test:room"))
}
