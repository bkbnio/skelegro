package org.leafygreens.skelegro.terragro.utils

class Heredoc(private val delimiter: String, private val template: String) {
  override fun toString(): String = "<<$delimiter\n$template\n$delimiter"
}
