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
    androidTestImplementation(Dependencies.Android.testRules)
    androidTestImplementation(Dependencies.Compose.uiTestJunit4)
    androidTestImplementation(Dependencies.Espresso.core)
    androidTestImplementation(Dependencies.Espresso.idilingResource)

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

    debugImplementation(Dependencies.Compose.uiTestManifest)
    debugImplementation(Dependencies.Compose.uiTooling)

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

    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.ktx)

    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.ktx)
    implementation(Dependencies.Compose.navigation)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.uiToolingPreview)

    implementation(Dependencies.Firebase.analytics)
    implementation(Dependencies.Firebase.authKtx)
    implementation(Dependencies.Firebase.bom)
    implementation(Dependencies.Firebase.databaseKtx)
    implementation(Dependencies.Firebase.storageKtx)

    implementation(Dependencies.Koin.compose)
    implementation(Dependencies.Koin.core)

    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Timber.core)

    implementation(Dependencies.Material.material)

    ksp(Dependencies.Room.compiler)

    testImplementation(Dependencies.Coroutine.test)

    testImplementation(Dependencies.Koin.koinTest)
    testImplementation(Dependencies.Koin.test)
    testImplementation(Dependencies.Koin.testJunit4)

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)

    testImplementation(Dependencies.Mockk.core)
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
