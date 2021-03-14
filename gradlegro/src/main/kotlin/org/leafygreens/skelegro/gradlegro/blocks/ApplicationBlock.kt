package org.leafygreens.skelegro.gradlegro.blocks

import org.leafygreens.skelegro.gradlegro.BuildGradleKts
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder
import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

class ApplicationBlock(
  private val mainClassName: String
) {
  override fun toString() = blockBuilder {
    appendLine("application {")
    appendLine("  @Suppress(${quoted("DEPRECATION")})")
    appendLine("  mainClassName = ${quoted(mainClassName)}")
    appendLine("}")
  }
}

fun BuildGradleKts.application(mainClassName: String): ApplicationBlock {
  val ab = ApplicationBlock(mainClassName)
  application = ApplicationBlock(mainClassName)
  return ab
}
