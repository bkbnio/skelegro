package org.leafygreens.skelegro.gradlegro.models

import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

sealed class Plugin

sealed class VersionedPlugin(private val version: String) : Plugin() {
  override fun toString() = "version ${quoted(version)}"
}

class HandledPlugin(private val module: String, private val version: String) : VersionedPlugin(version) {
  override fun toString() = "kotlin(${quoted(module)}) ${super.toString()}"
}

class StandardPlugin(private val id: String, private val version: String) : VersionedPlugin(version) {
  override fun toString() = "id(\"$id\") ${super.toString()}"
}

class BacktickPlugin(private val name: String) : Plugin() {
  override fun toString() = "`$name`"
}

class RawPlugin(private val plugin: String) : Plugin() {
  override fun toString() = plugin
}
