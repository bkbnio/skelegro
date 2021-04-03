package org.leafygreens.skelegro.gradlegro

import org.leafygreens.skelegro.gradlegro.blocks.AllProjectsBlock
import org.leafygreens.skelegro.gradlegro.blocks.ApplicationBlock
import org.leafygreens.skelegro.gradlegro.blocks.DependenciesBlock
import org.leafygreens.skelegro.gradlegro.blocks.PluginsBlock
import org.leafygreens.skelegro.gradlegro.blocks.RepositoriesBlock
import org.leafygreens.skelegro.gradlegro.blocks.TaskBlock
import org.leafygreens.skelegro.gradlegro.blocks.allprojects
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder
import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

open class BuildGradleKts(
  private val group: String,
  private val version: String,
  private val freestyle: String? = null,
  var allProjectsBlock: AllProjectsBlock? = null,
  var plugins: PluginsBlock? = null,
  var application: ApplicationBlock? = null,
  var repositories: RepositoriesBlock? = null,
  var dependencies: DependenciesBlock? = null,
  var tasks: TaskBlock? = null
) {
  override fun toString() = blockBuilder {
    appendLine("group = ${quoted(group)}")
    appendLine("version = ${quoted(version)}")
    appendLine()
    appendLine(allProjectsBlock ?: "")
    appendLine()
    appendLine(plugins ?: "")
    appendLine()
    appendLine(application ?: "")
    appendLine()
    appendLine(repositories ?: "")
    appendLine()
    appendLine(dependencies ?: "")
    appendLine()
    appendLine(tasks ?: "")
    appendLine()
    appendLine(freestyle ?: "")
  }
}

fun buildGradleKts(
  group: String,
  version: String,
  freestyle: String? = null,
  init: BuildGradleKts.() -> Unit
): BuildGradleKts {
  val bgk = BuildGradleKts(group, version, freestyle)
  bgk.init()
  return bgk
}
