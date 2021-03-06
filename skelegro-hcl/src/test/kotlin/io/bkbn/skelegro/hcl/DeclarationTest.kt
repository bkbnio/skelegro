package io.bkbn.skelegro.hcl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import io.bkbn.skelegro.hcl.TestData.getFileSnapshot
import io.bkbn.skelegro.hcl.utils.FunctionCall
import io.bkbn.skelegro.hcl.utils.HclType
import io.bkbn.skelegro.hcl.utils.Heredoc
import io.bkbn.skelegro.hcl.utils.Reference

@Suppress("LongMethod")
internal class DeclarationTest {

  @Test
  fun `Can build a full deployment manifest`() {
    // when
    val manifest = hclFile {
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

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("deployment.tf")
    assertEquals(expected, result)
  }

  @Test
  fun `Can build a namespace via DSL`() {
    // when
    val manifest = hclFile {
      "resource" label "kubernetes_namespace" label "vault" block {
        "metadata" block {
          "annotations" eqBlock {
            "name" eq "vault"
          }
          "labels" eqBlock {
            "role" eq "vault"
          }
          "name" eq "vault"
        }
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("namespace.tf")
    assertEquals(expected, result)
  }

  @Test
  fun `Can build providers via DSL`() {
    // when
    val manifest = hclFile {
      "terraform" block {
        "required_providers" block {
          "helm" eqBlock {
            "source" eq "hashicorp/helm"
            "version" eq "2.0.3"
          }
          "kubernetes" eqBlock {
            "source" eq "hashicorp/kubernetes"
            "version" eq "2.0.3"
          }
        }
      }
      `---`()
      "provider" label "helm" block {
        "kubernetes" block {
          "config_path" eq "~/.kube/config"
          "config_context" eq "my-cluster"
        }
      }
      `---`()
      "provider" label "kubernetes" block {
        "config_path" eq "~/.kube/config"
        "config_context" eq "my-cluster"
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("providers.tf")
    assertEquals(expected, result)
  }

  @Test
  fun `Can build a service via DSL with a resource reference`() {
    // when
    val manifest = hclFile {
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

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("service.tf")
    assertEquals(expected, result)
  }

  @Test
  fun `Can declare a Heredoc value`() {
    // when
    val manifest = hclFile {
      "resource" label "vault_generic_secret" label "my_secret" block {
        "data_json" eq Heredoc(
          "EOF", """
        {
          "test": "${"$"}{kubernetes_namespace.vault.count}",
        }
      """.trimIndent()
        )
        "path" eq "important/test"
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("heredoc.tf")
    assertEquals(expected, result)
  }

  @Test
  fun `Can declare a custom backend`() {
    // when
    val manifest = hclFile {
      "terraform" block {
        "backend" label "remote" block {
          "organization" eq "bkbnio"
          `---`()
          "workspaces" block {
            "name" eq "my-amazing-workspace"
          }
        }
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("backend.tf")
    assertEquals(expected, result)
  }

  @Test
  fun `Can declare a data type`() {
    // when
    val manifest = hclFile {
      "data" label "vault_generic_secret" label "my_super_secret_data" block {
        "path" eq "my/secret/path"
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("data.tf")
    assertEquals(expected, result)
  }
}
