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
    //noinspection GradleDependency
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.0.21")
}
