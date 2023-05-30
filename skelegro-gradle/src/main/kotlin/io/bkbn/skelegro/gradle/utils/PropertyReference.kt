package io.bkbn.skelegro.gradle.utils

class PropertyReference(private val resolver: Any, private val accessor: String) {
  override fun toString(): String {
    return "$resolver.$accessor"
  }
}
