plugins {
  kotlin("jvm")
  id("io.bkbn.sourdough.library.jvm")
  id("org.jetbrains.kotlinx.kover")
  id("io.gitlab.arturbosch.detekt")
  id("com.adarshr.test-logger")
  id("maven-publish")
  id("java-library")
  id("signing")
}

sourdoughLibrary {
  libraryName.set("Skelegro Gradle")
  libraryDescription.set("Kotlin DSL to generate Gradle .kts files")
}

dependencies {
  implementation("com.squareup:kotlinpoet:1.17.0")
}

testing {
  suites {
    named("test", JvmTestSuite::class) {
      useJUnitJupiter()
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
        implementation("io.mockk:mockk:1.13.11")
        implementation("com.google.truth:truth:1.4.2")
      }
    }
  }
}
