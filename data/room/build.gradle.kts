plugins {
    id(BuildIds.androidLibrary)
    id(BuildIds.ksp)
    kotlin(BuildIds.pluginKotlin)
    kotlin(BuildIds.pluginKapt)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
}

android {
    namespace = "com.nicholas.rutherford.track.your.shot.data.room"

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

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
    api(project(path = ":base-resources"))
    api(project(path = ":helper:constants"))

    androidTestImplementation(Dependencies.Junit.ext)
    androidTestImplementation(Dependencies.CoreTesting.core)
    androidTestImplementation(Dependencies.Espresso.core)
    androidTestImplementation(Dependencies.Truth.core)

    implementation(Dependencies.Gson.core)
    implementation(Dependencies.Room.ktx)
    implementation(Dependencies.Room.runtime)
    implementation(Dependencies.Room.coroutines)

    ksp(Dependencies.Room.compiler)

    testImplementation(Dependencies.Junit.core)

    androidTestImplementation(project(path = ":data-test:room"))
}
