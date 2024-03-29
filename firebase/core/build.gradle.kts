plugins {
    id(BuildIds.androidLibrary)
    kotlin(BuildIds.pluginKotlin)
    id(BuildIds.ktLintId) version Versions.Dependencies.KtLint.ktLint
    id(BuildIds.kover)
}

android {
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
    api(project(":data:firebase"))
    api(project(path = ":helper:constants"))
    api(project(path = ":helper:extensions"))

    implementation(Dependencies.Firebase.authKtx)
    implementation(Dependencies.Firebase.bom)
    implementation(Dependencies.Firebase.databaseKtx)
    implementation(Dependencies.Firebase.storageKtx)

    implementation(Dependencies.Timber.core)

    testImplementation(Dependencies.Coroutine.test)

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testImplementation(Dependencies.Mockk.core)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)

    testImplementation(project(":data-test:firebase"))
}
