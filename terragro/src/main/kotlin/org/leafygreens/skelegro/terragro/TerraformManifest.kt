package org.leafygreens.skelegro.terragro

class TerraformManifest {

  companion object {
    private const val TAB = "  "
  }

  private val sb = StringBuilder()
  private var level = 0

  infix fun String.eq(value: Any) = when (value) {
    is String -> sb.appendIndented("$this = \"$value\"")
    else -> sb.appendIndented("$this = $value")
  }

  infix fun String.label(value: String) = "$this \"$value\""

  infix fun String.block(block: () -> Unit): String {
    sb.appendIndented("$this {")
    level++
    block.invoke()
    level--
    sb.appendIndented("}")
    return this
  }

  fun `---`() {
    sb.appendLine()
  }

  override fun toString() = sb.toString()

  private fun addIndent(level: Int): String = TAB.repeat(level)
  private fun StringBuilder.appendIndented(value: Any) = appendLine("${addIndent(level)}$value")
}

fun terraformManifest(init: TerraformManifest.() -> Unit): TerraformManifest {
  val manifest = TerraformManifest()
  manifest.init()
  return manifest
}

fun main() {
  val test = terraformManifest {
    "data" label "vault_generic_secret" label "my_super_secret_data" block {
      "path" eq "my/secret/path"
    }
  }
  println(test.toString())
}
