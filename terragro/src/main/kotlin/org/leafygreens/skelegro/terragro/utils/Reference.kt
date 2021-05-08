package org.leafygreens.skelegro.terragro.utils

class Reference(private vararg val args: Any) {
  override fun toString(): String = args.joinToString(".")
}
