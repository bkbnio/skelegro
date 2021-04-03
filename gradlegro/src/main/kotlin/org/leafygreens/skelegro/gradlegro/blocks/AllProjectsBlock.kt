package org.leafygreens.skelegro.gradlegro.blocks

import org.leafygreens.skelegro.gradlegro.BuildGradleKts
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder

class AllProjectsBlock(
  var repositories: RepositoriesBlock? = null,
) {

  override fun toString() = blockBuilder {
    appendLine("allprojects {")
    repositories.toString().lines().forEach { appendLine("  $it") }
    appendLine("}")
  }
}

fun BuildGradleKts.allprojects(init: AllProjectsBlock.() -> Unit): AllProjectsBlock {
  val apb = AllProjectsBlock().apply(init)
  allProjectsBlock = apb
  return apb
}
