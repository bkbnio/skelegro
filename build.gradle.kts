import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm") version "1.5.10" apply false
  id("io.gitlab.arturbosch.detekt") version "1.17.1" apply false
  id("com.adarshr.test-logger") version "3.0.0" apply false
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0" apply true
  id("com.github.jakemarsden.git-hooks") version "0.0.2" apply true
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

  repositories {
    mavenCentral()
    mavenLocal()
  }

  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "io.gitlab.arturbosch.detekt")
  apply(plugin = "com.adarshr.test-logger")
  apply(plugin = "jacoco")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "signing")
  apply(plugin = "idea")

  tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.withType(JacocoReport::class))
  }

  configure<TestLoggerExtension> {
    theme = ThemeType.MOCHA
    logLevel = LogLevel.LIFECYCLE
    showExceptions = true
    showStackTraces = true
    showFullStackTraces = false
    showCauses = true
    slowThreshold = 2000
    showSummary = true
    showSimpleNames = false
    showPassed = true
    showSkipped = true
    showFailed = true
    showStandardStreams = false
    showPassedStandardStreams = true
    showSkippedStandardStreams = true
    showFailedStandardStreams = true
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "11"
    }
  }

  configure<DetektExtension> {
    toolVersion = "1.17.1"
    config = files("${rootProject.projectDir}/detekt.yml")
    buildUponDefaultConfig = true
  }

  configure<JavaPluginExtension> {
    withSourcesJar()
    withJavadocJar()
  }

  configure<PublishingExtension> {
    repositories {
      maven {
        name = "GithubPackages"
        url = uri("https://maven.pkg.github.com/bkbnio/skelegro")
        credentials {
          username = System.getenv("GITHUB_ACTOR")
          password = System.getenv("GITHUB_TOKEN")
        }
      }
    }
    publications {
      create<MavenPublication>("skelegro") {
        groupId = project.group.toString()
        artifactId = project.name.toLowerCase()
        version = project.version.toString()

        pom {
          name.set("Skelegro")
          description.set("A wacky assortment of Kotlin DSLs for infrastructure manifest generation")
          url.set("https://github.com/bkbnio/skelegro")
          licenses {
            license {
              name.set("MIT License")
              url.set("https://mit-license.org/")
            }
          }
          developers {
            developer {
              id.set("bkbnio")
              name.set("Ryan Brink")
              email.set("admin@bkbn.io")
            }
          }
          scm {
            connection.set("scm:git:git://github.com/bkbnio/skelegro.git")
            developerConnection.set("scm:git:ssh://github.com/bkbnio/skelegro.git")
            url.set("https://github.com/bkbnio/skelegro.git")
          }
        }
      }
    }
  }

  configure<JacocoPluginExtension> {
    toolVersion = "0.8.7"
  }

  // Should only happen on release, when publishing to nexus
  if ((project.findProperty("release") as? String)?.toBoolean() == true) {
    configure<SigningExtension> {
      val signingKey: String? by project
      val signingPassword: String? by project
      useInMemoryPgpKeys(signingKey, signingPassword)
      sign(extensions.getByType(PublishingExtension::class).publications)
    }
  }
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}

tasks.register<JacocoReport>("jacocoRootReport") {
  subprojects {
    this@subprojects.plugins.withType<JacocoPlugin>().configureEach {
      this@subprojects.tasks
        // TODO This is ghetto af lol fix this... for that matter.. track ITs??
        .matching { it.extensions.findByType<JacocoTaskExtension>() != null && it.name != "testIntegration" }
        .configureEach {
          sourceSets(this@subprojects.the<SourceSetContainer>().named("main").get())
          executionData(this)
        }
    }
  }

  reports {
    xml.isEnabled = true
    html.isEnabled = true
  }
}
