import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id(BuildIds.androidLibrary)
    id(BuildIds.ksp)
    kotlin(BuildIds.pluginKotlin)
    kotlin(BuildIds.pluginKapt)
    id(BuildIds.ktLintId) version ConfigurationData.ktlintVersion
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
        testInstrumentationRunner = ConfigurationData.testInstrumentationRunner

        //noinspection WrongGradleMethod
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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

    tasks.getByPath(TaskOptions.preBuildPath).dependsOn(TaskOptions.ktlintFormatPath)
}

dependencies {
    api(project(path = ":base-resources"))
    api(project(path = ":helper:constants"))

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.truth)

    implementation(libs.gson)
    implementation(libs.androidx.room.ktx)
    implementation(libs.room.runtime)
    implementation(libs.kotlinx.coroutines.android)

    ksp(libs.room.compiler)

    testImplementation(libs.junit)

    androidTestImplementation(project(path = ":data-test:room"))

    testImplementation(project(path = ":data-test:firebase"))
    testImplementation(project(path = ":data-test:room"))

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.android.junit5)

    testImplementation(libs.mockk)

    testRuntimeOnly(libs.junit.jupiter.engine)
}
