import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.spotbugs.snom.SpotBugsTask
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
  checkstyle
  id("com.github.spotbugs") version "6.5.9"
  id("com.gradleup.shadow") version "9.6.0"
  id("com.modrinth.minotaur") version "2.9.0"
  `java-library`
  java
}

group = "nl.oxod.oxmines"

fun getTime(): String {
  val sdf = SimpleDateFormat("yyMMdd-HHmm")
  sdf.timeZone = TimeZone.getTimeZone("Amsterdam")
  return sdf.format(Date())
}

version = (if (!hasProperty("ver")) {
  "${getTime()}-SNAPSHOT"
} else {
  val ver = property("ver") as String
  if (ver.startsWith("v")) ver.drop(1) else ver.replace('/', '-')
}).uppercase()

java.toolchain.languageVersion = JavaLanguageVersion.of(25)

repositories {
  maven {
    name = "papermc"
    url = uri("https://repo.papermc.io/repository/maven-public/")
    content {
      includeModule("io.papermc.paper", "paper-api")
      includeModule("net.md-5", "bungeecord-chat")
      includeModule("io.papermc.adventure", "adventure-api")
    }
  }

  maven {
    name = "minecraft"
    url = uri("https://libraries.minecraft.net")
    content {
      includeModule("com.mojang", "brigadier")
    }
  }

  maven {
    name = "worldedit"
    url = uri("https://maven.enginehub.org/repo/")
    content {
      includeModule("com.sk89q.worldedit", "worldedit-bukkit")
      includeModule("com.sk89q.worldedit", "worldedit-core")
      includeModule("com.sk89q.worldedit.worldedit-libs", "bukkit")
      includeModule("com.sk89q.worldedit.worldedit-libs", "core")
    }
  }

  mavenCentral()

  maven {
    name = "jitpack"
    url = uri("https://jitpack.io")
  }
}

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
  compileOnly("io.papermc.paper:paper-api:26.1.2.build.74-stable")
  implementation("org.bstats:bstats-bukkit:3.2.1")

  compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.13") {
    exclude(group = "com.google.guava", module = "guava")
    exclude(group = "com.google.code.gson", module = "gson")
    exclude(group = "it.unimi.dsi", module = "fastutil")
  }

  // Code quality and unit testing
  compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.3")
  spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0")
  testCompileOnly("com.github.spotbugs:spotbugs-annotations:4.10.3")
  testImplementation("io.papermc.paper:paper-api:26.1.2.build.74-stable")
  testImplementation("org.junit.jupiter:junit-jupiter:6.1.2")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.2")

  testImplementation("org.mockito:mockito-core:5.23.0")
  mockitoAgent("org.mockito:mockito-core:5.23.0") { isTransitive = false }
}

tasks.test {
  useJUnitPlatform()
  jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

tasks.processResources {
  filesMatching("**/plugin.yml") {
    expand(mapOf("NAME" to rootProject.name, "VERSION" to version, "PACKAGE" to project.group))
  }
}

checkstyle {
  toolVersion = "13.6.0"
  maxWarnings = 0
}

configurations.named("checkstyle") {
  resolutionStrategy.capabilitiesResolution
    .withCapability("com.google.collections:google-collections") {
      select("com.google.guava:guava:23.0")
    }
}

tasks.withType<Checkstyle>().configureEach {
  reports {
    xml.required.set(false)
    html.required.set(true)
  }
}

tasks.withType<SpotBugsTask>().configureEach {
  reports.create("html") {
    required.set(true)
  }
  reports.create("xml") {
    required.set(false)
  }
}

val shadowJarTask = tasks.named<ShadowJar>("shadowJar") {
  archiveClassifier.set("")
  archiveFileName.set("${rootProject.name}.jar")
  mergeServiceFiles()
  minimize {}

  configurations = project.configurations.runtimeClasspath.map { setOf(it) }

  dependencies {
    exclude { it.moduleGroup != "org.bstats" }
  }

  relocate("org.bstats", project.group.toString())
}

tasks.jar {
  enabled = false
}

modrinth {
  token.set(System.getenv("MODRINTH") ?: "")
  projectId.set("oxmines")
  versionNumber.set(project.version.toString())
  versionType.set("beta")
  
  uploadFile.set(shadowJarTask.flatMap { it.archiveFile })
  
  gameVersions.addAll("26.2", "26.1.1", "26.1.2", "26.1")
  changelog = System.getenv("CHANGELOG") ?: ""
  loaders.addAll("paper", "spigot", "bukkit")
  dependencies {
    optional.project("worldedit")
  }
}

tasks.modrinth {
  dependsOn(shadowJarTask)
}

tasks.register("release") {
  dependsOn(shadowJarTask)

  if (!System.getenv("MODRINTH").isNullOrEmpty()) {
    finalizedBy(tasks.modrinth)
  }
}
