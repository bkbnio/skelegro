plugins {
  id("io.bkbn.sourdough.root") version "0.2.9"
  id("com.github.jakemarsden.git-hooks") version "0.0.2" apply true
//  id("io.github.gradle-nexus.publish-plugin") version "1.1.0" apply true
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
  apply(plugin = "io.bkbn.sourdough.library")

  configure<io.bkbn.sourdough.gradle.core.extension.SourdoughLibraryExtension> {
    githubOrg.set("bkbnio")
    githubRepo.set("skelegro")
    libraryName.set("Skelegro")
    libraryDescription.set("A wacky assortment of Kotlin DSLs for infrastructure manifest generation")
    licenseName.set("MIT License")
    licenseUrl.set("https://mit-license.org")
    developerId.set("bkbnio")
    developerName.set("Ryan Brink")
    developerEmail.set("admin@bkbn.io")
  }
}

//nexusPublishing {
//  repositories {
//    sonatype {
//      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
//      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
//    }
//  }
//}
