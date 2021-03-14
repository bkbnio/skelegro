package org.leafygreens.skelegro.gradlegro.blocks

import org.leafygreens.skelegro.gradlegro.BuildGradleKts
import org.leafygreens.skelegro.gradlegro.models.Plugin
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder

class PluginsBlock(
  var plugins: MutableList<Plugin> = mutableListOf()
) {
  override fun toString() = blockBuilder {
    appendLine("plugins {")
    plugins.forEach { appendLine("  $it") }
    appendLine("}")
  }
}

fun BuildGradleKts.plugins(init: PluginsBlock.() -> Unit): PluginsBlock {
  val pb = PluginsBlock().apply(init)
  plugins = pb
  return pb
}

fun PluginsBlock.add(plugin: Plugin) = this.plugins.add(plugin)
