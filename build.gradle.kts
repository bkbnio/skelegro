plugins {
  id("org.jetbrains.kotlin.jvm") version "1.4.32" apply false
  id("io.gitlab.arturbosch.detekt") version "1.16.0-RC2" apply false
}

allprojects {
  group = "org.leafygreens"
  version = run {
    val baseVersion =
      project.findProperty("project.version") ?: error("project.version must be set in gradle.properties")
    when ((project.findProperty("release") as? String)?.toBoolean()) {
      true -> baseVersion
      else -> "$baseVersion-SNAPSHOT"
    }
  }

  repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    mavenCentral()
  }

  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "io.gitlab.arturbosch.detekt")
  apply(plugin = "java-library")
  apply(plugin = "maven-publish")
  apply(plugin = "idea")

  tasks.withType<Test>() {
    useJUnitPlatform()
  }

  tasks.withType<AbstractTestTask> {
    testLogging {
      setExceptionFormat("full")
    }
    afterSuite(
      KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
        if (desc.parent == null) { // will match the outermost suite
          println(
            "Results: ${result.resultType} (${result.testCount} tests, " +
              "${result.successfulTestCount} successes, " +
              "${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
          )
        }
      })
    )
  }

  tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = "14"
    }
  }

  configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    toolVersion = "1.16.0-RC2"
    config = files("${rootProject.projectDir}/detekt.yml")
    buildUponDefaultConfig = true
  }

  configure<PublishingExtension> {
    repositories {
      maven {
        name = "GithubPackages"
        url = uri("https://maven.pkg.github.com/lg-backbone/skelegro")
        credentials {
          username = System.getenv("GITHUB_ACTOR")
          password = System.getenv("GITHUB_TOKEN")
        }
      }
    }
    publications {
      create<MavenPublication>("skelegro") {
        from(components["kotlin"])
      }
    }
  }

  configure<JavaPluginExtension> {
    withSourcesJar()
  }
}
