package org.leafygreens.skelegro.gradlegro.blocks

import com.squareup.kotlinpoet.ClassName
import org.leafygreens.skelegro.gradlegro.utils.Helpers.TAB
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder
import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

sealed class TaskTypeBlock

class BuildTaskBlock(
  val dependsOn: MutableList<String> = mutableListOf()
) : TaskTypeBlock() {
  override fun toString() = blockBuilder {
    appendLine("build {")
    dependsOn.forEach { appendLine("  dependsOn(${quoted(it)})") }
    appendLine("}")
  }
}

fun TaskBlock.buildBlock(init: BuildTaskBlock.() -> Unit): BuildTaskBlock {
  val b = BuildTaskBlock().apply(init)
  taskTypes.add(b)
  return b
}

fun BuildTaskBlock.dependsOn(task: String) {
  dependsOn += task
}

class WithTypeBlock(private val classDeclaration: ClassName, var guts: TaskTypeBlock? = null) : TaskTypeBlock() {
  override fun toString() = blockBuilder {
    appendLine("withType($classDeclaration::class).all {")
    guts.toString().lines().forEach { appendLine("  $it") }
    appendLine("}")
  }
}

fun TaskBlock.withType(classDeclaration: ClassName, init: WithTypeBlock.() -> Unit): WithTypeBlock {
  val wtb = WithTypeBlock(classDeclaration).apply(init)
  taskTypes.add(wtb)
  return wtb
}

class CompileKotlinTaskBlock(private val jvmTarget: String) : TaskTypeBlock() {
  override fun toString() = blockBuilder {
    appendLine("kotlinOptions {")
    appendLine("${TAB}jvmTarget = ${quoted(jvmTarget)}")
    appendLine("}")
  }
}

fun WithTypeBlock.kotlinOptions(jvmTarget: String): CompileKotlinTaskBlock {
  val cktb = CompileKotlinTaskBlock(jvmTarget)
  guts = cktb
  return cktb
}

class NamedTypeBlock(
  private val classDeclaration: ClassName,
  var name: String? = null,
  var guts: TaskTypeBlock? = null
) : TaskTypeBlock() {
  override fun toString() = blockBuilder {
    appendLine("named<$classDeclaration>(${quoted(name ?: error("Must have a task name"))}) {")
    guts.toString().lines().forEach { appendLine("  $it") }
    appendLine("}")
  }
}

class ShadowJarTaskBlock(private val classifier: String? = null) : TaskTypeBlock() {
  override fun toString() = blockBuilder {
    appendLine("@Suppress(${quoted("DEPRECATION")})")
    if (classifier != null) {
      appendLine("classifier = ${quoted(classifier)}")
    } else {
      appendLine("classifier = $classifier")
    }
  }
}

fun TaskBlock.named(classDeclaration: ClassName, init: NamedTypeBlock.() -> Unit): NamedTypeBlock {
  val ntb = NamedTypeBlock(classDeclaration).apply(init)
  taskTypes.add(ntb)
  return ntb
}

fun NamedTypeBlock.shadow(classifier: String? = null) {
  name = "shadowJar"
  guts = ShadowJarTaskBlock(classifier)
}
