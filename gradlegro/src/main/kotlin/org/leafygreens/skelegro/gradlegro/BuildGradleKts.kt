package org.leafygreens.skelegro.gradlegro

import org.leafygreens.skelegro.gradlegro.blocks.AllProjectsBlock
import org.leafygreens.skelegro.gradlegro.blocks.ApplicationBlock
import org.leafygreens.skelegro.gradlegro.blocks.DependenciesBlock
import org.leafygreens.skelegro.gradlegro.blocks.PluginsBlock
import org.leafygreens.skelegro.gradlegro.blocks.RepositoriesBlock
import org.leafygreens.skelegro.gradlegro.blocks.TaskBlock
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder
import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

class BuildGradleKts(
  private val group: String? = null,
  private val version: String? = null,
  private val freestyle: String? = null,
  var allProjectsBlock: AllProjectsBlock? = null,
  var plugins: PluginsBlock? = null,
  var application: ApplicationBlock? = null,
  var repositories: RepositoriesBlock? = null,
  var dependencies: DependenciesBlock? = null,
  var tasks: TaskBlock? = null
) {
  override fun toString() = blockBuilder {
    if (group != null) appendLine("group = ${quoted(group)}")
    if (version != null) appendLine("version = ${quoted(version)}")
    if (allProjectsBlock != null) appendLine(allProjectsBlock)
    if (plugins != null) appendLine(plugins)
    if (application != null) appendLine(application)
    if (repositories != null) appendLine(repositories)
    if (dependencies != null) appendLine(dependencies)
    if (tasks != null) appendLine(tasks)
    if (freestyle != null) appendLine(freestyle)
  }
}

fun buildGradleKts(
  group: String? = null,
  version: String? = null,
  freestyle: String? = null,
  init: BuildGradleKts.() -> Unit
): BuildGradleKts {
  val bgk = BuildGradleKts(group, version, freestyle)
  bgk.init()
  return bgk
}
