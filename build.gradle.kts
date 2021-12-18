plugins {
  id("io.bkbn.sourdough.root") version "0.2.6"
  id("com.github.jakemarsden.git-hooks") version "0.0.2" apply true
}

sourdough {
  toolChainJavaVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.majorVersion))
  jvmTarget.set(JavaVersion.VERSION_11.majorVersion)
  compilerArgs.set(listOf("-opt-in=kotlin.RequiresOptIn"))
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
  apply(plugin = "maven-publish")
  apply(plugin = "signing")
  apply(plugin = "java")
  apply(plugin = "java-library")
}
