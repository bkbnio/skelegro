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
  libraryName.set("Skelegro HCL")
  libraryDescription.set("Kotlin DLS for generating HCL files")
}

testing {
  suites {
    named("test", JvmTestSuite::class) {
      useJUnitJupiter()
      dependencies {
        implementation("org.junit.jupiter:junit-jupiter-api:5.11.2")
        implementation("io.mockk:mockk:1.13.13")
        implementation("com.google.truth:truth:1.4.4")
      }
    }
  }
}
