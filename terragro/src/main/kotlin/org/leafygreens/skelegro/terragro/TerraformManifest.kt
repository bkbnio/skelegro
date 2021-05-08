package org.leafygreens.skelegro.terragro

import org.leafygreens.skelegro.terragro.utils.FunctionCall

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

  fun fn(name: String, vararg args: Any): String = FunctionCall(name).withArguments(*args).toString()

  override fun toString() = sb.toString()

  private fun addIndent(level: Int): String = TAB.repeat(level)
  private fun StringBuilder.appendIndented(value: Any) = appendLine("${addIndent(level)}$value")
}

fun terraformManifest(init: TerraformManifest.() -> Unit): TerraformManifest {
  val manifest = TerraformManifest()
  manifest.init()
  return manifest
}

class Reference(private vararg val args: Any) {
  override fun toString(): String = args.joinToString(".")
}

enum class HclType {
  STRING;

  override fun toString(): String = this.name.toLowerCase()
}

fun main() {
  val test = terraformManifest {
    "variable" label "github_token" block {
      "type" eq HclType.STRING
      "sensitive" eq true
    }
    `---`()
    "resource" label "kubernetes_deployment" label "potato_app" block {
      "metadata" block {
        "name" eq FunctionCall("base64decode").withArguments(
          Reference("data", "digitalocean_kubernetes_cluster", "cluster", "kube_config", 0, "cluster_ca_certificate")
        )
        "labels" eqBlock {
          "application" eq "potato-app"
          "owner" eq "big-boss"
        }
      }
      "spec" block {
        "replicas" eq 3
        "selector" block {
          "match_labels" eqBlock {
            "application" eq "potato-app"
            "owner" eq "big-boss"
          }
        }
        "template" block {
          "metadata" block {
            "labels" eqBlock {
              "application" eq "potato-app"
              "owner" eq "big-boss"
            }
          }
          "spec" block {
            "image_pull_secrets" block {
              "name" eq "ghcr"
            }
            "container" block {
              "image" eq "my-image:latest"
              "name" eq "potato-app"
              "image_pull_policy" eq "Always"
              "port" block {
                "container_port" eq 8080
                "protocol" eq "TCP"
              }
              "env" block {
                "name" eq "MY_SPECIAL_ENV_VAR"
                "value" eq Reference("data", "vault_generic_secret", "credentials", "data[\"token\"]")
              }
              "env" block {
                "name" eq "GITHUB_TOKEN"
                "value" eq Reference("var", "github_token")
              }
              "resources" block {
                "limits" eqBlock {
                  "cpu" eq "1"
                  "memory" eq "1024Mi"
                }
                "requests" eqBlock {
                  "cpu" eq "0.5"
                  "memory" eq "512Mi"
                }
              }
              "liveness_probe" block {
                "http_get" block {
                  "path" eq "/"
                  "port" eq 8080
                }
                "initial_delay_seconds" eq 30
                "period_seconds" eq 30
              }
            }
          }
        }
      }
    }
  }
  println(test.toString())
}
