import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	id("dev.architectury.loom") version "1.6.397"
	id("maven-publish")
	id("com.github.johnrengelman.shadow") version "8.1.1"
	id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

java {
	withSourcesJar()
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "21"
}

version = rootProject.property("mod_version").toString()
group = rootProject.property("maven_group").toString()

repositories {
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
	maven("https://maven.notenoughupdates.org/releases/")
}

val shadowImpl: Configuration by configurations.creating {
	configurations.implementation.get().extendsFrom(this)
}

val shadowModImpl: Configuration by configurations.creating {
	configurations.modImplementation.get().extendsFrom(this)
}

dependencies {
	"minecraft"("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
	"mappings"("net.fabricmc:yarn:${rootProject.property("yarn_mappings")}:v2")

	modImplementation("net.fabricmc:fabric-loader:${rootProject.property("loader_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric_kotlin_version")}")
	modApi("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_version")}")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	modImplementation("org.notenoughupdates.moulconfig:modern:3.0.0-beta.11")
	include("org.notenoughupdates.moulconfig:modern:3.0.0-beta.11")

	implementation("com.github.kwhat:jnativehook:2.2.2")
	include("com.github.kwhat:jnativehook:2.2.2")
}

loom {
	runs {
		named("client") {
			property("devauth.enabled", "true")
			property("mixin.debug", "true")
		}
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.release.set(21)
}

tasks.jar {
	destinationDirectory.set(layout.buildDirectory.dir("badjars"))
	archiveClassifier.set("without-deps")
}

tasks.shadowJar {
	destinationDirectory.set(layout.buildDirectory.dir("badjars"))
	archiveClassifier.set("all-dev")
	configurations = listOf()
}

tasks.remapJar {
	inputFile.set(tasks.shadowJar.get().archiveFile)
	dependsOn(tasks.shadowJar)
	archiveClassifier.set("")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(inputs.properties)
	}
}