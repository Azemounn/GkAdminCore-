plugins {
    kotlin("jvm") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "7.1.2" // For building fat JARs
}

group = "com.gameknight.admincore"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot API
    maven("https://oss.sonatype.org/content/repositories/snapshots/") // Snapshots
    maven("https://oss.sonatype.org/content/repositories/releases/") // Releases
}

dependencies {
    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")

    // Spigot/Bukkit API
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT") // Match your Spigot server version

    // Guava for utilities
    implementation("com.google.guava:guava:31.1-jre")

    // SnakeYAML for advanced YAML parsing
    implementation("org.yaml:snakeyaml:1.33")

    // Testing framework
    testImplementation("junit:junit:4.13.2")
}

tasks {
    // Kotlin compiler settings
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17" // Target Java 17, required for Minecraft 1.21.3
        }
    }

    // Java compiler settings
    compileJava {
        options.release.set(17) // Ensure Java 17 compatibility
    }

    // Shadow JAR task
    shadowJar {
        archiveClassifier.set("") // Outputs the main JAR
        configurations = listOf(project.configurations.runtimeClasspath.get())
        minimize() // Reduce JAR size by minimizing dependencies
    }

    // Standard JAR task (if not using Shadow JAR)
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

    test {
        useJUnitPlatform()
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Set Java toolchain to version 17
    }
}
