plugins {
    id(BuildIds.androidLibrary)
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

    ktlint {
        disabledRules.value(mutableListOf("no-wildcard-imports"))
    }
}

dependencies {
    api(project(path = ":base-resources"))
    api(project(path = ":compose:components"))
    api(project(path = ":data:room"))
    api(project(path = ":firebase:create"))
    api(project(path = ":firebase:read"))
    api(project(path = ":firebase:util"))
    api(project(path = ":helper:compose-content-test-rule"))
    api(project(path = ":helper:constants"))
    api(project(path = ":helper:extensions"))
    api(project(path = ":helper:network"))
    api(project(path = ":helper:ui"))
    api(project(path = ":navigation"))
    api(project(path = ":shared-preference"))

    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.viewModel)

    testImplementation(Dependencies.Coroutine.test)

    testImplementation(Dependencies.Junit.Jupiter.api)
    testImplementation(Dependencies.Junit.Jupiter.params)
    testImplementation(Dependencies.Junit.junit)

    testImplementation(Dependencies.Mockk.core)

    testRuntimeOnly(Dependencies.Junit.Jupiter.engine)

    testImplementation(project(path = ":data-test:account-info"))
}
