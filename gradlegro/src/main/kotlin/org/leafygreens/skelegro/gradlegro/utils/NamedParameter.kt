package org.leafygreens.skelegro.gradlegro.utils

class NamedParameter(private val key: String, private val value: Any) {
  override fun toString(): String = when (value) {
    is String -> "$key = \"$value\""
    else -> "$key = $value"
  }
}
