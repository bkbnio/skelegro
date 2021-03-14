package org.leafygreens.skelegro.gradlegro.utils

object Helpers {

  const val TAB = "  "
  fun quoted(guts: String) = "\"$guts\""

  fun blockBuilder(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }
}
