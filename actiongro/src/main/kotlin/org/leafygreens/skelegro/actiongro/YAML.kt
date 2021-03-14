package org.leafygreens.skelegro.actiongro

interface YAML {
  fun buildEntity(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }
}
