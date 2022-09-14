plugins {
    id(BuildIds.pluginId)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
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
            resValue(
                type = types.BuildTypes.Res.string,
                name = types.BuildTypes.Res.app_name,
                value = types.BuildTypes.UniqueBuilds.Release.appName
            )
        }

        getByName(types.BuildTypes.UniqueBuilds.Debug.buildName) {
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Debug.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Debug.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Debug.isDebuggable
            resValue(
                type = types.BuildTypes.Res.string,
                name = types.BuildTypes.Res.app_name,
                value = types.BuildTypes.UniqueBuilds.Debug.appName
            )
        }

        create(types.BuildTypes.UniqueBuilds.Staging.buildName) {
            /**
             * To copies the Build Name [types.BuildTypes.UniqueBuilds.Debug] and
             * allows me to customize attributes
             **/
            initWith(getByName(types.BuildTypes.UniqueBuilds.Debug.buildName))
            applicationIdSuffix = types.BuildTypes.UniqueBuilds.Staging.applicationIdSuffix
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Staging.isMinifyEnabled
            isDebuggable = types.BuildTypes.UniqueBuilds.Staging.isDebuggable
            resValue(
                type = types.BuildTypes.Res.string,
                name = types.BuildTypes.Res.app_name,
                value = types.BuildTypes.UniqueBuilds.Staging.appName
            )
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
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.ktx)

    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.material)

    implementation(Dependencies.Koin.core)

    implementation(Dependencies.Material.material)

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testImplementation(Dependencies.Koin.test)
    testImplementation(Dependencies.Koin.testJunit4)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)
}
