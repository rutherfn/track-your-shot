plugins {
    id(BuildIds.pluginId)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
    id(BuildIds.googleServices)
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
            androidResources { isCrunchPngs = false }

            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appLabel] = types.BuildTypes.UniqueBuilds.Release.appName
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.appIcon] = types.BuildTypes.UniqueBuilds.Release.appIconRoute
            manifestPlaceholders[types.BuildTypes.ManifiestOptions.roundAppIcon] = types.BuildTypes.UniqueBuilds.Release.roundAppIconRoute
        }

        getByName(types.BuildTypes.UniqueBuilds.Debug.buildName) {
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Debug.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Debug.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Debug.isDebuggable

            androidResources { isCrunchPngs = false }

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

            androidResources { isCrunchPngs = false }

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
        testInstrumentationRunnerArguments[ConfigurationData.runnerBuilder] = ConfigurationData.androidJunit5Builder

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
}

dependencies {
    api(project(path = ":app-center"))
    api(project(path = ":build-type"))

    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.ktx)

    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.material)

    implementation(Firebase.bom)

    implementation(Dependencies.Koin.core)

    implementation(Dependencies.Material.material)

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testImplementation(Dependencies.Koin.test)
    testImplementation(Dependencies.Koin.testJunit4)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)
}
