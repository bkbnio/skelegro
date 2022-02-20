package io.bkbn.skelegro.gradle.utils

class ClassReference(private val name: String) {
  override fun toString(): String = "$name::class"
}
