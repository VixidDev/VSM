import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	id("fabric-loom") version "1.6-SNAPSHOT"
	id("maven-publish")
	id("com.github.johnrengelman.shadow") version "8.1.1"
	id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

version = rootProject.property("mod_version").toString()
group = rootProject.property("maven_group").toString()

repositories {
	mavenCentral()

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
	// To change the versions see the gradle.properties file
	"minecraft"("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
	"mappings"("net.fabricmc:yarn:${rootProject.property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${rootProject.property("loader_version")}")

	modApi("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric_kotlin_version")}")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	shadowImpl("com.github.kwhat:jnativehook:2.2.2")
	shadowModImpl("org.notenoughupdates.moulconfig:modern:3.0.0-beta.11")
}

loom {
	runs {
		named("client") {
			property("devauth.enabled", "true")
			property("mixin.debug", "true")
		}
	}
}

tasks.shadowJar {
	configurations = listOf(shadowModImpl)
	relocate("io.github.notenoughupdates.moulconfig", "dev.vixid.vsm.deps.moulconfig")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(inputs.properties)
	}
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "21"
}

java {
	withSourcesJar()
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}