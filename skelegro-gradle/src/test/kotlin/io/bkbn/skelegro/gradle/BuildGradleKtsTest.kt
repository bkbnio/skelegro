package io.bkbn.skelegro.gradle

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.bkbn.skelegro.gradle.util.Helpers.getFileSnapshot
import io.bkbn.skelegro.gradle.utils.EnumReference
import io.bkbn.skelegro.gradle.utils.FunctionCall
import io.bkbn.skelegro.gradle.utils.NamedParameter

internal class BuildGradleKtsTest {

  @Test
  fun `Can build a gradle script equivalent to the root project build file`() {
    // when
    val buildScript = buildGradleKts {
      "plugins" block {
        +(fn("id", "org.jetbrains.kotlin.jvm") version "1.4.32" apply false)
        +(fn("id", "io.gitlab.arturbosch.detekt") version "1.16.0-RC2" apply false)
        +(fn("id", "com.adarshr.test-logger") version "3.0.0" apply false)
      }
      `---`()
      "allprojects" block {
        "group" eq "org.leafygreens"
        "version" eq "0.0.1"
        `---`()
        "repositories" block {
          "maven" block {
            "url" eq FunctionCall("uri").withArguments("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
          }
          +fn("mavenCentral")
        }
        `---`()
        +fn("apply", NamedParameter("plugin", "org.jetbrains.kotlin.jvm"))
        +fn("apply", NamedParameter("plugin", "io.gitlab.arturbosch.detekt"))
        +fn("apply", NamedParameter("plugin", "com.adarshr.test-logger"))
        +fn("apply", NamedParameter("plugin", "java-library"))
        +fn("apply", NamedParameter("plugin", "maven-publish"))
        +fn("apply", NamedParameter("plugin", "idea"))
        `---`()
        fn("tasks.withType<Test>") block {
          +fn("useJUnitPlatform")
        }
        `---`()
        "configure<com.adarshr.gradle.testlogger.TestLoggerExtension>" block {
          "theme" eq EnumReference("com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA")
          "logLevel" eq EnumReference("LogLevel.LIFECYCLE")
          "showExceptions" eq true
          "showStackTraces" eq true
          "showFullStackTraces" eq false
          "showCauses" eq true
          "slowThreshold" eq 2000
          "showSummary" eq true
          "showSimpleNames" eq false
          "showPassed" eq true
          "showSkipped" eq true
          "showFailed" eq true
          "showStandardStreams" eq false
          "showPassedStandardStreams" eq true
          "showSkippedStandardStreams" eq true
          "showFailedStandardStreams" eq true
        }
        `---`()
        fn("tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>").plus(".configureEach") block {
          "kotlinOptions" block {
            "jvmTarget" eq "11"
          }
        }
        `---`()
        "configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>" block {
          "toolVersion" eq "1.16.0-RC2"
          "config" eq FunctionCall("files").withArguments("\${rootProject.projectDir}/detekt.yml")
          "buildUponDefaultConfig" eq true
        }
        `---`()
        "configure<PublishingExtension>" block {
          "repositories" block {
            "maven" block {
              "name" eq "GithubPackages"
              "url" eq FunctionCall("uri").withArguments("https://maven.pkg.github.com/bkbnio/skelegro")
              "credentials" block {
                "username" eq FunctionCall("System.getenv").withArguments("GITHUB_ACTOR")
                "password" eq FunctionCall("System.getenv").withArguments("GITHUB_TOKEN")
              }
            }
          }
        }
        `---`()
        "configure<JavaPluginExtension>" block {
          +fn("withSourcesJar")
        }
      }
    }

    // expect
    val expected = getFileSnapshot("ExampleRootBuildScript.txt")
    assertEquals(expected, buildScript.toString().trim())
  }

  @Test
  fun `Can build a settings gradle kts file`() {
    // when
    val settingsFile = buildGradleKts {
      "rootProject.name" eq "skelegro"
      +fn("include", "dockergro", "gradlegro", "actiongro", "terragro")
      +fn("enableFeaturePreview", "VERSION_CATALOGS")
    }

    // expect
    val expected = getFileSnapshot("ExampleSettingsFile.txt")
    assertEquals(expected, settingsFile.toString().trim())
  }

}
