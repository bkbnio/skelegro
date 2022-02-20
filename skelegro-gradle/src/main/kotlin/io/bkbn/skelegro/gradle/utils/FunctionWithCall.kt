package io.bkbn.skelegro.gradle.utils

class FunctionWithCall(private val fn: FunctionCall, private val call: String) {
  override fun toString() = "$fn.$call"
}
