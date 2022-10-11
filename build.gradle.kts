plugins {
  kotlin("jvm") version "1.6.21" apply false
  id("io.bkbn.sourdough.root") version "0.9.1"
  id("com.github.jakemarsden.git-hooks") version "0.0.2"
  id("org.jetbrains.dokka") version "1.7.20"
  id("org.jetbrains.kotlinx.kover") version "0.5.1"
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
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
