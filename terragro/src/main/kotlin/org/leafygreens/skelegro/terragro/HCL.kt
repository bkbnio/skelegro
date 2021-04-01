package org.leafygreens.skelegro.terragro

interface HCL {
  fun buildEntity(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }
}
