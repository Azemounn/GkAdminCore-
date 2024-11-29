plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2" // For building fat JARs
}

group = "com.gameknight.admincore"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") } // Spigot API
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") } // Snapshots
    maven { url = uri("https://oss.sonatype.org/content/repositories/releases/") } // Releases
}

dependencies {
    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")

    // Spigot/Bukkit API
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT") // Adjust for your server version

    // Guava (for utility functions)
    implementation("com.google.guava:guava:31.1-jre")

    // Configurable Yaml (optional)
    implementation("org.yaml:snakeyaml:1.33")

    // Shadow plugin dependencies (for building fat JAR)
    testImplementation("junit:junit:4.13.2")
}

tasks {
    // Kotlin compiler settings
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17" // Target Java 17, as required by Minecraft 1.21.3
        }
    }

    // Java compiler settings
    compileJava {
        options.release.set(17) // Match Minecraft's Java 17 requirement
    }

    // Shadow JAR task for fat JARs
    shadowJar {
        archiveClassifier.set("") // Output as main JAR
        configurations = listOf(project.configurations.runtimeClasspath.get())
        minimize() // Reduce JAR size
    }

    // Standard JAR task
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
        })
    }

    processResources {
        // Replace placeholders in plugin.yml
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Use Java 17 toolchain
    }
}
