package org.leafygreens.skelegro.terragro

import org.leafygreens.skelegro.terragro.utils.FunctionCall
import org.leafygreens.skelegro.terragro.utils.Heredoc
import org.leafygreens.skelegro.terragro.utils.Reference

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

  infix fun String.eqBlock(block: () -> Unit): String {
    sb.appendIndented("$this = {")
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
    "resource" label "kubernetes_service" label "my_service" block {
      "metadata" block {
        "name" eq "my-service"
      }
      "spec" block {
        "selector" eqBlock {
          "application" eq Reference("kubernetes_deployment", "my_app", "metadata", 0, "labels", "application")
          "owner" eq Reference("kubernetes_deployment", "my_app", "metadata", 0, "labels", "owner")
        }
        "port" block {
          "port" eq 80
          "target_port" eq 8080
        }
      }
    }
  }
  println(test.toString())
}
