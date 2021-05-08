package io.bkbn.skelegro.hcl.utils

enum class HclType {
  STRING;

  override fun toString(): String = this.name.toLowerCase()
}
