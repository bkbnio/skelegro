package org.leafygreens.skelegro.terragro

@TerraformManifestDslMarker
class TerraformManifest(var declarations: MutableList<Declaration> = mutableListOf()) {
  private fun buildEntity(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }

  override fun toString(): String = buildEntity {
    declarations.forEach { appendLine(it.toString()) }
  }
}

fun terraformManifest(init: TerraformManifest.() -> Unit): TerraformManifest {
  val manifest = TerraformManifest()
  manifest.init()
  return manifest
}
