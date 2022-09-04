plugins {
    id(BuildIds.pluginId)
    kotlin(BuildIds.pluginKotlin)
}

android {
    buildToolsVersion = ConfigurationData.buildToolsVersion
    compileSdk = ConfigurationData.compileSdk

    buildTypes {
        getByName(types.BuildTypes.UniqueBuilds.Release.buildName) {
            isMinifyEnabled = types.BuildTypes.UniqueBuilds.Release.isMinifyEnabled
            proguardFiles(
                getDefaultProguardFile(types.BuildTypes.proguardAndroidOptimizeTxt),
                types.BuildTypes.proguardRulesPro
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
    implementation(Dependencies.Material.material)

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)
}
