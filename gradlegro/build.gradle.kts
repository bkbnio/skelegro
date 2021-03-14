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
  `java-library-distribution`
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

  // This dependency is used internally, and not exposed to consumers on their own compile classpath.
  implementation("com.google.guava:guava:29.0-jre")

  implementation("com.squareup:kotlinpoet:1.7.2")

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
  distributions {
    main {
      distributionBaseName.set(rootProject.name)
    }
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
      url = uri("https://maven.pkg.github.com/rgbrizzlehizzle/proboscidea")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("runtime") {
      from(components["kotlin"])
      artifact(tasks["shadowJar"])
    }
  }
}
