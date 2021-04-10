package org.leafygreens.skelegro.gradlegro.models

import org.leafygreens.skelegro.gradlegro.utils.Helpers.quoted

sealed class Repository

class StandardRepository(private val name: String) : Repository() {
  override fun toString() = "$name()"
}

class CustomRepository(private val name: String, private val url: String) : Repository() {
  override fun toString() = "$name(${quoted(url)})"
}

val MAVEN_LOCAL = StandardRepository("mavenLocal")
val MAVEN_CENTRAL = StandardRepository("mavenCentral")
