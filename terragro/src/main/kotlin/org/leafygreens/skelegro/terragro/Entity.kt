package org.leafygreens.skelegro.terragro

interface Entity {
  fun buildEntity(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }
}
