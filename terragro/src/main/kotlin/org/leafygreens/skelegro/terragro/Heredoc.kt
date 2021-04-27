package org.leafygreens.skelegro.terragro

data class Heredoc(val delimiter: String, val content: String) {
  override fun toString(): String = "<<$delimiter\n$content\n$delimiter"
}
