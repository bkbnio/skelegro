package org.leafygreens.actiongro

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.leafygreens.actiongro.ActionEntityExtensions.arrayEntity
import org.leafygreens.actiongro.ActionEntityExtensions.nestedEntities
import org.leafygreens.actiongro.ActionEntityExtensions.nestedEntity
import org.leafygreens.actiongro.ActionEntityExtensions.stringEntity
import org.leafygreens.actiongro.TestData.getFileSnapshot

internal class GithubActionTest {

  @Test
  fun `Can build a file that will kick of a github action to assemble a gradle package`() {
    // when
    val action = githubAction {
      stringEntity {
        key = "name"
        value = "Testing"
      }
      nestedEntity {
        key = "on"
        value = nestedEntity {
          key = "push"
          value = arrayEntity<String> {
            key = "branches"
            values = mutableListOf(Pair("main", true))
          }
        }
      }
      nestedEntities {
        key = "env"
        stringEntity {
          key = "GITHUB_USER"
          value = "\${{ github.actor }}"
        }
        stringEntity {
          key = "GITHUB_TOKEN"
          value = "\${{ secrets.GITHUB_TOKEN }}"
        }
      }
      nestedEntity {
        key = "jobs"
        value = nestedEntities {
          key = "assemble"
          stringEntity {
            key = "runs-on"
            value = "ubuntu-latest"
          }
          arrayEntity<ActionEntity> {
            key = "steps"
            values = mutableListOf(
              Pair(ActionEntity.StringEntity("uses", "actions/checkout@v2"), true),
              Pair(ActionEntity.StringEntity("uses", "actions/setup-java@v1"), true),
              Pair(ActionEntity.NestedEntity("with", ActionEntity.StringEntity("java-version", "1.14")), false),
              Pair(ActionEntity.StringEntity("name", "Cache Gradle Packages"), true),
              Pair(ActionEntity.StringEntity("uses", "actions/cache@v2"), false),
              Pair(
                ActionEntity.NestedEntities(
                  "with", mutableListOf(
                    ActionEntity.StringEntity("path", "~/.gradle/caches"),
                    ActionEntity.StringEntity("key", "\${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle.kts') }}"),
                    ActionEntity.StringEntity("restore-keys", "\${{ runner.os }}-gradle")
                  )
                ), false
              ),
              Pair(ActionEntity.StringEntity("name", "Assemble Gradle"), true),
              Pair(ActionEntity.StringEntity("run", "gradle assemble"), false)
            )
          }
        }
      }
    }

    // expect
    val expected = getFileSnapshot("GradleAssembleExample.yaml")
    assertEquals(action.toString().trim(), expected.trim())
  }

}
