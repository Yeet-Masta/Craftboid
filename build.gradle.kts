import org.gradle.jvm.tasks.Jar
import java.util.Properties

plugins {
    kotlin("jvm") version "2.1.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.joml:joml:1.9.2")
    implementation("org.yaml:snakeyaml:2.4")
    implementation("org.fusesource.jansi:jansi:2.4.1")
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))

    tasks.named<Jar>("jar") {
        destinationDirectory.set(project.file("build"))
        archiveFileName.set("Patch_Files.jar")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(configurations.runtimeClasspath.get().map { file ->
            if (file.isDirectory) {
                file
            } else {
                zipTree(file)
            }
        })
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}