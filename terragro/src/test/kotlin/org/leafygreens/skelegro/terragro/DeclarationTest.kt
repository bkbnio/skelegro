package org.leafygreens.skelegro.terragro

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

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
  fun `Can build a resource declaration`() {
    // when
    val entities = mutableListOf<DeclarationEntity>(
      DeclarationEntity.Object(
        "metadata", values = mutableListOf(
          DeclarationEntity.Simple("name", "backbone-generator"),
          DeclarationEntity.Map(
            "labels", mutableListOf(
              DeclarationEntity.Simple("application", "backbone-generator"),
              DeclarationEntity.Simple("owner", "leafygreens-backbone")
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
          name = "backbone-generator"
          labels = {
            application = "backbone-generator"
            owner = "leafygreens-backbone"
          }
        }
        spec {
        }
      }
    """.trimIndent()
    assertThat(result).isEqualTo(expected)
  }

}
