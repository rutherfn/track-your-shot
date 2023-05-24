plugins {
    id(BuildIds.pluginId)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
    id(BuildIds.gmsGoogleServices)
}

android {
    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    buildFeatures {
        compose = ComposeData.Enabled.value
    }

    composeOptions {
        kotlinCompilerExtensionVersion = ComposeData.KotlinCompiler.extensionVersion
    }

    buildTypes {
        getByName(types.BuildTypes.UniqueBuilds.Release.buildName) {
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Release.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Release.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Release.isDebuggable
            proguardFiles(
                getDefaultProguardFile(types.BuildTypes.proguardAndroidOptimizeTxt),
                types.BuildTypes.proguardRulesPro
            )

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

    tasks.getByPath(TaskOptions.preBuildPath).dependsOn(TaskOptions.ktlintFormatPath)
}

dependencies {
    implementation("com.google.android.play:core-ktx:1.8.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.3.0")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation(Dependencies.Compose.uiTestJunit4)

    api(project(path = ":app-center"))
    api(project(path = ":base-resources"))
    api(project(path = ":build-type"))
    api(project(path = ":feature:create-account"))
    api(project(path = ":feature:forgot-password"))
    api(project(path = ":feature:home"))
    api(project(path = ":feature:login"))
    api(project(path = ":feature:splash"))
    api(project(path = ":helper:compose-content-test-rule"))
    api(project(path = ":helper:network"))

    debugImplementation(Dependencies.Compose.uiTestManifest)

    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.ktx)

    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.navigation)
    implementation(Dependencies.Compose.material)

    implementation("io.insert-koin:koin-androidx-compose:3.2.0")

    implementation(Dependencies.Firebase.authKtx)
    implementation(Dependencies.Firebase.bom)
    implementation(Dependencies.Firebase.databaseKtx)

    implementation(Dependencies.Koin.core)

    implementation(Dependencies.Material.material)

    testImplementation(Dependencies.Coroutine.test)

    testImplementation(Dependencies.Koin.koinTest)
    testImplementation(Dependencies.Koin.test)
    testImplementation(Dependencies.Koin.testJunit4)

    androidTestImplementation("org.mockito:mockito-core:3.12.4")

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)

    testImplementation(Dependencies.Mockk.core)
}
