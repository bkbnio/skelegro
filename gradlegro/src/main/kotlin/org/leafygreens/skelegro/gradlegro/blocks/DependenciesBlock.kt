package org.leafygreens.skelegro.gradlegro.blocks

import org.leafygreens.skelegro.gradlegro.BuildGradleKts
import org.leafygreens.skelegro.gradlegro.models.Dependency
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder

class DependenciesBlock(
  var dependencies: MutableList<Dependency> = mutableListOf()
) {
  override fun toString() = blockBuilder {
    appendLine("dependencies {")
    dependencies.forEach { appendLine("  $it") }
    appendLine("}")
  }
}

fun BuildGradleKts.dependencies(init: DependenciesBlock.() -> Unit): DependenciesBlock {
  val db = DependenciesBlock().apply(init)
  dependencies = db
  return db
}

fun DependenciesBlock.add(dependency: Dependency) = this.dependencies.add(dependency)
