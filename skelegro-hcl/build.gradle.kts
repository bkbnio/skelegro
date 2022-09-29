plugins {
  kotlin("jvm")
  id("io.bkbn.sourdough.library.jvm") version "0.6.0"
  id("io.gitlab.arturbosch.detekt") version "1.19.0"
  id("com.adarshr.test-logger") version "3.2.0"
  id("org.jetbrains.dokka")
  id("maven-publish")
  id("java-library")
  id("signing")
}

sourdough {
  githubOrg.set("bkbnio")
  githubRepo.set("skelegro")
  libraryName.set("Skelegro HCL")
  libraryDescription.set("Kotlin DLS for generating HCL files")
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
        implementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
        implementation("io.mockk:mockk:1.13.2")
        implementation("com.google.truth:truth:1.1.3")
      }
    }
  }
}
