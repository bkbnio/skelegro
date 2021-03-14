package org.leafygreens.skelegro.gradlegro.blocks

import org.leafygreens.skelegro.gradlegro.BuildGradleKts
import org.leafygreens.skelegro.gradlegro.models.Repository
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder

class RepositoriesBlock(
  var repositories: MutableList<Repository> = mutableListOf()
) {
  override fun toString() = blockBuilder {
    appendLine("repositories {")
    repositories.forEach { appendLine("  $it") }
    appendLine("}")
  }
}

fun BuildGradleKts.repositories(init: RepositoriesBlock.() -> Unit): RepositoriesBlock {
  val rb = RepositoriesBlock().apply(init)
  repositories = rb
  return rb
}

fun RepositoriesBlock.add(repository: Repository) = this.repositories.add(repository)
