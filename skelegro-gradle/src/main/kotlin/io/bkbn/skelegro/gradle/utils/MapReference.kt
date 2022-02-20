package io.bkbn.skelegro.gradle.utils

class MapReference(private val name: String, private val key: String) {
  override fun toString(): String = "$name[\"$key\"]"
}
