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
    val stdibVersion = "1.9.10"

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$stdibVersion")
}
