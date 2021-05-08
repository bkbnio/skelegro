package io.bkbn.skelegro.hcl.utils

class Reference(private vararg val args: Any) {
  override fun toString(): String = args.joinToString(".")
}
