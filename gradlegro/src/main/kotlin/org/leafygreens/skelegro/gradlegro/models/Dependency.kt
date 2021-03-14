package org.leafygreens.skelegro.gradlegro.models

import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

sealed class Dependency

sealed class ImplementationDependency : Dependency() {
  fun toString(guts: String) = "implementation($guts)"
}

sealed class TestImplementationDependency : Dependency() {
  fun toString(guts: String) = "testImplementation($guts)"
}

class DependencyHandler(private val handler: String, private val module: String) : ImplementationDependency() {
  override fun toString() = super.toString("$handler(${quoted(module)})")
}

class HandledDependency(private val group: String, private val name: String) : ImplementationDependency() {
  override fun toString() = super.toString(quoted("$group:$name"))
}

class StandardImplementationDependency(
  private val group: String,
  private val name: String,
  private val version: String
) : ImplementationDependency() {
  override fun toString() = super.toString(quoted("$group:$name:$version"))
}

class StandardTestImplementationDependency(
  private val group: String,
  private val name: String,
  private val version: String
) : TestImplementationDependency() {
  override fun toString() = super.toString(quoted("$group:$name:$version"))
}
