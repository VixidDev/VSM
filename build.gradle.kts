import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("fabric-loom") version "1.10-SNAPSHOT"
	id ("maven-publish")
	id("org.jetbrains.kotlin.jvm") version "2.1.21"
	id("com.gradleup.shadow") version "8.3.4"
}

version = rootProject.property("mod_version").toString()
group = rootProject.property("maven_group").toString()

base {
	archivesName = rootProject.property("archives_base_name").toString()
}

java {
	withSourcesJar()
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

val shadowModImpl: Configuration by configurations.creating {
	configurations.modImplementation.get().extendsFrom(this)
}

repositories {
	maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
	maven("https://maven.notenoughupdates.org/releases/")
}

dependencies {
	minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${rootProject.property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${rootProject.property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${rootProject.property("fabric_kotlin_version")}")

	shadowModImpl("org.notenoughupdates.moulconfig:modern-1.21.5:3.11.0")
	shadowModImpl("com.github.kwhat:jnativehook:2.2.2")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(inputs.properties)
	}
}

tasks.withType<JavaCompile> {
	options.release = 21
}

tasks.withType<KotlinCompile> {
	compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

tasks.shadowJar {
	destinationDirectory.set(layout.buildDirectory.dir("badjars"))
	archiveClassifier.set("all-dev")
	configurations = listOf(shadowModImpl)
	relocate("io.github.notenoughupdates.moulconfig", "dev.vixid.vsm.deps.moulconfig")
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archives_name"]}" }
	}
}