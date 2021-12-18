plugins {
  id("io.bkbn.sourdough.root") version "0.1.1"
  id("com.github.jakemarsden.git-hooks") version "0.0.2" apply true
}

sourdough {
  toolChainJavaVersion = JavaVersion.VERSION_17
  jvmTarget = JavaVersion.VERSION_11.majorVersion
  compilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
}

gitHooks {
  setHooks(
    mapOf(
      "pre-commit" to "detekt",
      "pre-push" to "test"
    )
  )
}

allprojects {
  group = "io.bkbn"
  version = run {
    val baseVersion =
      project.findProperty("project.version") ?: error("project.version must be set in gradle.properties")
    when ((project.findProperty("release") as? String)?.toBoolean()) {
      true -> baseVersion
      else -> "$baseVersion-SNAPSHOT"
    }
  }
}

subprojects {
  apply(plugin = "io.bkbn.sourdough.library")
  configure<io.bkbn.sourdough.gradle.core.extension.SourdoughLibraryExtension> {
    githubOrg = "bkbnio"
    githubRepo = "skelegro"
    githubUsername = System.getenv("GITHUB_ACTOR")
    githubToken = System.getenv("GITHUB_TOKEN")
    libraryName = "Skelegro"
    libraryDescription = "A wacky assortment of Kotlin DSLs for infrastructure manifest generation"
    licenseName = "MIT License"
    licenseUrl = "https://mit-license.org/"
    developerId = "bkbnio"
    developerName = "Ryan Brink"
    developerEmail = "admin@bkbn.io"
  }
}
