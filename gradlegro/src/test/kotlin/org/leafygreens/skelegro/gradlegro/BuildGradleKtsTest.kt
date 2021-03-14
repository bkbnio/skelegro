package org.leafygreens.skelegro.gradlegro

import com.google.common.truth.Truth.assertThat
import com.squareup.kotlinpoet.ClassName
import org.junit.jupiter.api.Test
import org.leafygreens.skelegro.gradlegro.blocks.add
import org.leafygreens.skelegro.gradlegro.blocks.application
import org.leafygreens.skelegro.gradlegro.blocks.buildBlock
import org.leafygreens.skelegro.gradlegro.blocks.dependencies
import org.leafygreens.skelegro.gradlegro.blocks.dependsOn
import org.leafygreens.skelegro.gradlegro.blocks.kotlinOptions
import org.leafygreens.skelegro.gradlegro.blocks.named
import org.leafygreens.skelegro.gradlegro.blocks.plugins
import org.leafygreens.skelegro.gradlegro.blocks.repositories
import org.leafygreens.skelegro.gradlegro.blocks.shadow
import org.leafygreens.skelegro.gradlegro.blocks.tasks
import org.leafygreens.skelegro.gradlegro.blocks.withType
import org.leafygreens.skelegro.gradlegro.models.CustomRepository
import org.leafygreens.skelegro.gradlegro.models.DependencyHandler
import org.leafygreens.skelegro.gradlegro.models.HandledDependency
import org.leafygreens.skelegro.gradlegro.models.HandledPlugin
import org.leafygreens.skelegro.gradlegro.models.JCENTER
import org.leafygreens.skelegro.gradlegro.models.MAVEN_LOCAL
import org.leafygreens.skelegro.gradlegro.models.RawPlugin
import org.leafygreens.skelegro.gradlegro.models.StandardImplementationDependency
import org.leafygreens.skelegro.gradlegro.models.StandardPlugin
import org.leafygreens.skelegro.gradlegro.models.StandardTestImplementationDependency
import org.leafygreens.skelegro.gradlegro.util.Helpers.getFileSnapshot

internal class BuildGradleKtsTest {

  @Test
  fun `Can build a gradle file that I want it to`() {
    val kotlinVersion = "1.4.21"
    val buildFile = buildGradleKts(
      group = "org.leafygreens",
      version = "0.0.1",
    ) {
      plugins {
        add(HandledPlugin("jvm", kotlinVersion))
        add(HandledPlugin("kapt", kotlinVersion))
        add(StandardPlugin("com.github.johnrengelman.shadow", "6.0.0"))
        add(RawPlugin("application"))
        add(RawPlugin("maven"))
      }
      application("org.leafygreens.backbone.engine.MainKt")
      repositories {
        add(JCENTER)
        add(CustomRepository("maven", "https://jitpack.io"))
        add(CustomRepository("github", "https://maven.pkg.github.com/rgbrizzlehizzle/blue-whale"))
        add(MAVEN_LOCAL)
      }
      dependencies {
        add(DependencyHandler("kotlin", "stdlib"))
        add(StandardImplementationDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.4.2"))
        add(DependencyHandler("platform", "software.amazon.awssdk:bom:2.14.9"))
        add(HandledDependency("software.amazon.awssdk", "s3"))
        add(StandardTestImplementationDependency("org.junit.jupiter", "junit-jupiter", "5.6.2"))
      }
      tasks {
        buildBlock {
          dependsOn("shadowJar")
        }
        withType(ClassName("org.jetbrains.kotlin.gradle.tasks", "KotlinCompile")) {
          kotlinOptions("14")
        }
        named(ClassName("com.github.jengelman.gradle.plugins.shadow.tasks", "ShadowJar")) {
          shadow()
        }
      }
    }

    val expected = getFileSnapshot("buildGradleKtsTest.txt")
    assertThat(buildFile.toString()).isEqualTo(expected.trim())
  }
}
