package org.leafygreens.skelegro.gradlegro

class BuildGradleKts {

  companion object {
    private const val TAB = "  "
  }

  private fun addIndent(level: Int): String = TAB.repeat(level)

  private val sb = StringBuilder()
  private var level = 0

  infix fun String.eq(value: Any) = when (value) {
    is String -> sb.appendIndented("$this = \"$value\"")
    else -> sb.appendIndented("$this = $value")
  }

  infix fun String.block(block: () -> Unit): String {
    sb.appendIndented("$this {")
    level++
    block.invoke()
    level--
    sb.appendIndented("}")
    return this
  }

  operator fun String.unaryPlus() {
    sb.appendIndented(this)
  }

  fun `---`() {
    sb.appendLine()
  }

  infix fun String.version(v: String) = "$this version \"$v\""

  infix fun String.apply(a: Boolean) = "$this apply $a"

  fun fn(name: String, vararg args: Any): String = FunctionCall(name).withArguments(*args).toString()

  override fun toString() = sb.toString()

  private fun StringBuilder.appendIndented(value: Any) = appendLine("${addIndent(level)}$value")
}

fun buildGradleKts(
  init: BuildGradleKts.() -> Unit
): BuildGradleKts {
  val bgk = BuildGradleKts()
  bgk.init()
  return bgk
}

fun main() {
  val rootGradleBuildKts = buildGradleKts {
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
          "url" eq fn("uri", "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
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
      fn("configure<com.adarshr.gradle.testlogger.TestLoggerExtension>") block {
        "theme" eq EnumReference("com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA")
        "logLevel" eq EnumReference("LogLevel.LIFECYCLE")
        "showExceptions" eq true
        "showStackTraces" eq true
        "showCauses" eq true
        "showFullStackTraces" eq false
        "slowThreshold" eq 2000
        "showSummary" eq true
        "showSimpleNames" eq false
        "showPassed" eq true
        "showFailed" eq true
        "showStandardStreams" eq false
        "showPassedStandardStreams" eq true
        "showSkippedStandardStreams" eq true
        "showFailedStandardStreams" eq true
      }
      `---`()
      fn("tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>").plus(".configureEach") block {
        "kotlinOptions" block {
          "jvmTarget" eq "14"
        }
      }
      `---`()
      "configure<io.gitlab.arturbosch.detekt.extensions.DetektExtensions>" block {
        "toolVersion" eq "1.16.0-RC2"
        "config" eq FunctionCall("files").withArguments("\${rootProject.projectDir}/detekt.yml")
        "buildUponDefaultConfig" eq true
      }
      `---`()
      "configure<PublishingExtension>" block {
        "repositories" block {
          "maven" block {
            "name" eq "GithubPackages"
            "url" eq FunctionCall("uri").withArguments("https://maven.pkg.github.com/lg-backbone/skelegro")
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
  println(rootGradleBuildKts.toString())
}

class NamedParameter(private val key: String, private val value: Any) {
  override fun toString(): String = when (value) {
    is String -> "$key = \"$value\""
    else -> "$key = $value"
  }
}
