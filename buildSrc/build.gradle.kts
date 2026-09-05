import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    // Must match the Kotlin version embedded in the Gradle distribution used by buildSrc.
    // Project modules use kotlin from libs.versions.toml independently.
    //noinspection GradleDependency
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.21")
}
