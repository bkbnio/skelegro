package org.leafygreens.skelegro.gradlegro

import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder
import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

class SettingsGradleKts(
  private val rootProjectName: String,
  val includedProjects: MutableList<String> = mutableListOf()
) {
  override fun toString() = blockBuilder {
    appendLine("rootProject.name = ${quoted(rootProjectName)}")
    includedProjects.forEach { appendLine("include(${quoted(it)})") }
  }
}

fun settingsGradleKts(rootProjectName: String, init: SettingsGradleKts.() -> Unit): SettingsGradleKts {
  val sgk = SettingsGradleKts(rootProjectName)
  sgk.init()
  return sgk
}

fun SettingsGradleKts.include(projectName: String) {
  includedProjects.add(projectName)
}
