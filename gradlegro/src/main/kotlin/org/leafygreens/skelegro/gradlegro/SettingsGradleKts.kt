package org.leafygreens.skelegro.gradlegro

import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder
import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

class SettingsGradleKts(
  private val rootProjectName: String
) {
  override fun toString() = blockBuilder {
    appendLine("rootProject.name = ${quoted(rootProjectName)}")
  }
}
