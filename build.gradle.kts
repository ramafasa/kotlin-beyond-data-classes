plugins {
    kotlin("jvm") version "2.4.20"
}

group = "pl.rafalmaciak.kotlin-modelling"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    dependencies {
        implementation("org.apache.poi:poi:5.5.1")
        implementation("org.apache.poi:poi-ooxml:5.5.1")
        testImplementation("io.kotest:kotest-runner-junit5:6.2.5")
        testImplementation("io.kotest:kotest-assertions-core:6.2.5")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(25)
}