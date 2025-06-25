plugins {
    id(BuildIds.pluginId)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
    id(BuildIds.gmsGoogleServices)
    id(BuildIds.ksp)
    id(BuildIds.kover)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.nicholas.rutherford.track.your.shot"

    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    buildFeatures {
        compose = ComposeData.Enabled.value
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ComposeData.KotlinCompiler.extensionVersion
    }

    buildTypes {
        getByName(types.BuildTypes.UniqueBuilds.Release.buildName) {
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Release.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Release.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Release.isDebuggable

            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appLabel] = types.BuildTypes.UniqueBuilds.Release.appName
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appIcon] = types.BuildTypes.UniqueBuilds.Release.appIconRoute
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.roundAppIcon] = types.BuildTypes.UniqueBuilds.Release.roundAppIconRoute
        }

        getByName(types.BuildTypes.UniqueBuilds.Debug.buildName) {
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Debug.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Debug.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Debug.isDebuggable

            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appLabel] = types.BuildTypes.UniqueBuilds.Debug.appName
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appIcon] = types.BuildTypes.UniqueBuilds.Debug.appIconRoute
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.roundAppIcon] = types.BuildTypes.UniqueBuilds.Debug.roundAppIconRoute
        }

        create(types.BuildTypes.UniqueBuilds.Stage.buildName) {
            /**
             * To copies the Build Name [types.BuildTypes.UniqueBuilds.Debug] and
             * allows me to customize attributes
             **/
            initWith(getByName(types.BuildTypes.UniqueBuilds.Debug.buildName))
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Stage.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Stage.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Stage.isDebuggable

            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appLabel] = types.BuildTypes.UniqueBuilds.Stage.appName
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appIcon] = types.BuildTypes.UniqueBuilds.Stage.appIconRoute
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.roundAppIcon] = types.BuildTypes.UniqueBuilds.Stage.roundAppIconRoute
        }
    }

    compileOptions {
        sourceCompatibility = types.BuildTypes.CompileOptions.sourceCompatibility
        targetCompatibility = types.BuildTypes.CompileOptions.targetCompatibility
    }

    defaultConfig {
        applicationId = BuildIds.applicationId

        minSdk = ConfigurationData.minSdk
        targetSdk = ConfigurationData.targetSdk

        testInstrumentationRunner = ConfigurationData.testInstrumentationRunner

        versionCode = ConfigurationData.versionCode
        versionName = ConfigurationData.versionName
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = KotlinOptions.jvmTarget
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    ktlint {
        disabledRules.value(mutableListOf("no-wildcard-imports"))
    }
}

dependencies {
    implementation(libs.protolite.well.known.types)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(Dependencies.Compose.uiTestJunit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.idling.resource)

    api(project(path = ":base-resources"))
    api(project(path = ":build-type"))
    api(project(path = ":feature:create-account"))
    api(project(path = ":feature:forgot-password"))
    api(project(path = ":feature:login"))
    api(project(path = ":feature:players"))
    api(project(path = ":feature:players:shots"))
    api(project(path = ":feature:reports"))
    api(project(path = ":feature:settings"))
    api(project(path = ":feature:shots"))
    api(project(path = ":feature:splash"))
    api(project(path = ":helper:account"))
    api(project(path = ":helper:compose-content-test-rule"))
    api(project(path = ":helper:file-generator"))
    api(project(path = ":helper:network"))
    api(project(path = ":helper:notifications"))

    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)

    kover(project(":build-type"))
    kover(project(":feature:create-account"))
    kover(project(":feature:forgot-password"))
    kover(project(":feature:login"))
    kover(project(":feature:players"))
    kover(project(":feature:reports"))
    kover(project(":feature:settings"))
    kover(project(":feature:splash"))
    kover(project(":firebase:core"))
    kover(project(":helper:network"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(Dependencies.Compose.navigation)
    implementation(Dependencies.Compose.material)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.storage.ktx)

    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)

    implementation(libs.room.runtime)
    implementation(libs.timber)

    implementation(libs.material)

    ksp(libs.room.compiler)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.core)
    testImplementation(libs.koin.test.junit4)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.android.junit5)

    testRuntimeOnly(libs.junit.jupiter.engine)

    testImplementation(libs.mockk)
}

koverReport {
    // filters for all report types of all build variants
    filters {
        excludes {
            classes(
                "*ScreenKt\$*",
                "*ScreenKt",
                "*ScreenParams\$*",
                "*ScreenParams",
                "*Tags\$*",
                "*Tags",
                "*.NavigationComponentKt",
                "*NavigationComponentKt\$*",
                "*.ViewModels",
                "*.MainActivityWrapper",
                "*Activity",
                "*Activity\$*",
                "*.BuildConfig"
            )
        }
    }
}
