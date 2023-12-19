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
  implementation("com.squareup:kotlinpoet:1.15.3")
}

testing {
  suites {
    named("test", JvmTestSuite::class) {
      useJUnitJupiter()
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
        implementation("io.mockk:mockk:1.13.8")
        implementation("com.google.truth:truth:1.2.0")
      }
    }
  }
}
