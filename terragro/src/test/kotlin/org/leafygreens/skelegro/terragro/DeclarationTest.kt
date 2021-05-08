package org.leafygreens.skelegro.terragro

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.leafygreens.skelegro.terragro.DeclarationEntityExtensions.entityMap
import org.leafygreens.skelegro.terragro.DeclarationEntityExtensions.hcl
import org.leafygreens.skelegro.terragro.DeclarationEntityExtensions.keyVal
import org.leafygreens.skelegro.terragro.DeclarationEntityExtensions.objectEntity
import org.leafygreens.skelegro.terragro.DeclarationExtensions.dataDeclaration
import org.leafygreens.skelegro.terragro.DeclarationExtensions.providerDeclaration
import org.leafygreens.skelegro.terragro.DeclarationExtensions.resourceDeclaration
import org.leafygreens.skelegro.terragro.DeclarationExtensions.terraformDeclaration
import org.leafygreens.skelegro.terragro.DeclarationExtensions.variableDeclaration
import org.leafygreens.skelegro.terragro.TestData.getFileSnapshot
import org.leafygreens.skelegro.terragro.utils.FunctionCall
import org.leafygreens.skelegro.terragro.utils.HclType
import org.leafygreens.skelegro.terragro.utils.Heredoc
import org.leafygreens.skelegro.terragro.utils.Reference

internal class DeclarationTest {

  @Test
  fun `Can build variable declaration`() {
    // when
    val declaration = Declaration.Variable("github_token", type = VariableType.STRING, sensitive = true)

    // do
    val result = declaration.toString()

    // expect
    val expected = """
      variable "github_token" {
        type = string
        sensitive = true
      }
    """.trimIndent()
    assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `Can build a provider declaration`() {
    // when
    val entities = mutableListOf<DeclarationEntity>(
      DeclarationEntity.Object(
        "kubernetes",
        mutableListOf(
          DeclarationEntity.Simple("config_path", "~/.kube/config"),
          DeclarationEntity.Simple("config_context", "my-cluster")
        )
      )
    )
    val declaration = Declaration.Provider("helm", entities)

    // do
    val result = declaration.toString()

    // expect
    val expected = """
      provider "helm" {
        kubernetes {
          config_path = "~/.kube/config"
          config_context = "my-cluster"
        }
      }
    """.trimIndent()
    assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `Can build a terraform declaration`() {
    // when
    val entities = mutableListOf<DeclarationEntity>(
      DeclarationEntity.Object(
        "required_providers", mutableListOf(
          DeclarationEntity.Map(
            "helm", mutableListOf(
              DeclarationEntity.Simple("source", "hashicorp/helm"),
              DeclarationEntity.Simple("version", "2.0.3")
            )
          )
        )
      )
    )
    val declaration = Declaration.Terraform(entities)

    // do
    val result = declaration.toString()

    // expect
    val expected = """
      terraform {
        required_providers {
          helm = {
            source = "hashicorp/helm"
            version = "2.0.3"
          }
        }
      }
    """.trimIndent()
    assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `Can build a resource declaration`() {
    // when
    val entities = mutableListOf<DeclarationEntity>(
      DeclarationEntity.Object(
        "metadata", values = mutableListOf(
          DeclarationEntity.Simple("name", "potato-app"),
          DeclarationEntity.Map(
            "labels", mutableListOf(
              DeclarationEntity.Simple("application", "potato-app"),
              DeclarationEntity.Simple("owner", "big-boss")
            )
          )
        )
      ),
      DeclarationEntity.Object("spec")
    )
    val declaration = Declaration.Resource("kubernetes_deployment", "backbone_generator", entities)

    // do
    val result = declaration.toString()

    // expect
    val expected = """
      resource "kubernetes_deployment" "backbone_generator" {
        metadata {
          name = "potato-app"
          labels = {
            application = "potato-app"
            owner = "big-boss"
          }
        }
        spec {
        }
      }
    """.trimIndent()
    assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `Can build a variable declaration using a DSL`() {
    // when
    val manifest = terraformManifest {
      variableDeclaration("github_token") {
        type = VariableType.STRING
        sensitive = true
      }

      resourceDeclaration("kubernetes_deployment", "backbone_generator") {
        objectEntity("metadata") {
          keyVal("name", "potato-app")
          entityMap<String>("labels") {
            keyVal("application", "potato-app")
            keyVal("owner", "big-boss")
          }
        }
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = """
      variable "github_token" {
        type = string
        sensitive = true
      }
      resource "kubernetes_deployment" "backbone_generator" {
        metadata {
          name = "potato-app"
          labels = {
            application = "potato-app"
            owner = "big-boss"
          }
        }
      }
    """.trimIndent()
    assertThat(result).isEqualTo(expected)
  }

  @Test
  fun `Can build a full deployment manifest`() {
    // when
    val manifest = terraformManifest {
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
    assertEquals(expected.trim(), result)
  }

  @Test
  fun `Can build a namespace via DSL`() {
    // when
    val manifest = terraformManifest {
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
    assertThat(result).isEqualTo(expected.trim())
  }

  @Test
  fun `Can build providers via DSL`() {
    // when
    val manifest = terraformManifest {
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
    assertThat(result).isEqualTo(expected.trim())
  }

  @Test
  fun `Can build a service via DSL with a resource reference`() {
    // when
    val manifest = terraformManifest {
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
    assertThat(result).isEqualTo(expected.trim())
  }

  @Test
  fun `Can declare a Heredoc value`() {
    // when
    val manifest = terraformManifest {
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
    assertThat(result).isEqualTo(expected.trim())
  }

  @Test
  fun `Can declare a custom backend`() {
    // when
    val manifest = terraformManifest {
      "terraform" block {
        "backend" label "remote" block {
          "organization" eq "lg-backbone"
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
    assertThat(result).isEqualTo(expected.trim())
  }

  @Test
  fun `Can declare a data type`() {
    // when
    val manifest = terraformManifest {
      "data" label "vault_generic_secret" label "my_super_secret_data" block {
        "path" eq "my/secret/path"
      }
    }

    // do
    val result = manifest.toString()

    // expect
    val expected = getFileSnapshot("data.tf")
    assertThat(result).isEqualTo(expected.trim())
  }
}
