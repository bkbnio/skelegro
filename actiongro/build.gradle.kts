group = "org.leafygreens"
version = run {
  val baseVersion =
    project.findProperty("project.version") ?: error("project.version must be set in gradle.properties")
  when ((project.findProperty("release") as? String)?.toBoolean()) {
    true -> baseVersion
    else -> "$baseVersion-SNAPSHOT"
  }
}

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.4.20"
  id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
  id("com.github.johnrengelman.shadow") version "6.1.0"
  `java-library`
  `maven-publish`
}

repositories {
  // Use JCenter for resolving dependencies.
  jcenter()
}

dependencies {
  // Align versions of all Kotlin components
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

  // Use the Kotlin JDK 8 standard library.
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

  val junitVersion = "5.6.2"
  val truthVersion = "1.0.1"

  // Junit
  testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
  testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

  // Truth
  testImplementation("com.google.truth:truth:$truthVersion")
}

tasks {
  test {
    useJUnitPlatform()
  }
  build {
    dependsOn("ktlintFormat")
  }
  named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    @Suppress("DEPRECATION")
    classifier = "shadow"
  }
}

publishing {
  repositories {
    maven {
      name = "GithubPackages"
      url = uri("https://maven.pkg.github.com/rgbrizzlehizzle/vin-diesel")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("source") {
      from(components["kotlin"])
      artifact(tasks["shadowJar"])
    }
  }
}
