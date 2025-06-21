plugins {
    id(BuildIds.androidLibrary)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
    id(BuildIds.kover)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.nicholas.rutherford.track.your.shot.feature.players"

    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    buildFeatures {
        compose = ComposeData.Enabled.value
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ComposeData.KotlinCompiler.extensionVersion
    }

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
}

dependencies {
    api(project(path = ":base:vm"))
    api(project(path = ":compose:components"))
    api(project(path = ":data:room"))
    api(project(path = ":feature:players:shots"))
    api(project(path = ":firebase:core"))
    api(project(path = ":firebase:util"))
    api(project(path = ":helper:account"))
    api(project(path = ":helper:constants"))
    api(project(path = ":helper:extensions"))
    api(project(path = ":navigation"))

    debugImplementation(libs.androidx.ui.tooling.preview)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(libs.coil.compose)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.materialDesignIconsCore)
    implementation(Dependencies.Compose.materialDesignIconsExtended)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(Dependencies.Compose.viewModel)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.android.junit5)

    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(project(":data-test:firebase"))

    testImplementation(project(path = ":data-test:room"))
}
