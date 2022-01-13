plugins {
  kotlin("jvm")
  id("io.bkbn.sourdough.library.jvm") version "0.5.5"
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  id("com.adarshr.test-logger") version "3.1.0"
  id("org.jetbrains.dokka")
  id("maven-publish")
  id("java-library")
  id("signing")
}

sourdough {
  githubOrg.set("bkbnio")
  githubRepo.set("skelegro")
  libraryName.set("Skelegro YML")
  libraryDescription.set("Kotlin DSL for generating YML Files")
  licenseName.set("MIT License")
  licenseUrl.set("https://mit-license.org")
  developerId.set("unredundant")
  developerName.set("Ryan Brink")
  developerEmail.set("admin@bkbn.io")
}

testing {
  suites {
    named("test", JvmTestSuite::class) {
      useJUnitJupiter()
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
        implementation("io.mockk:mockk:1.10.0")
        implementation("com.google.truth:truth:1.0.1")
      }
    }
  }
}
