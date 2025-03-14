plugins {
    kotlin("jvm") version "2.1.10"
}

group = "pl.rafalmaciak.kotlin-modelling"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    dependencies {
        implementation("org.apache.poi:poi:5.2.3")
        implementation("org.apache.poi:poi-ooxml:5.2.3")
        testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
        testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}