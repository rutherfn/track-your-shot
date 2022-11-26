import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    kotlin("jvm") version "1.7.0"
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly(gradleApi())

    implementation(kotlin("stdlib"))
}
