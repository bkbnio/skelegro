package io.bkbn.skelegro.yml

import io.bkbn.skelegro.yml.utils.Helpers.getFileSnapshot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Suppress("LongMethod")
internal class YamlFileTest {

  @Test
  fun `Can build a github action manifest with multiple jobs`() {
    // when
    val manifest = yamlFile {
      "name" eq "Build and Deploy"
      "on" block {
        "push" block {
          "branches" block {
            - "main"
          }
        }
      }
      "env" block {
        "ACTOR" eq "\${{ github.actor }}"
        "SECRET" eq "\${{ github.secret }}"
      }
      "jobs" block {
        "assemble" block {
          "runs-on" eq "ubuntu-latest"
          "steps" block {
            - ("uses" req "actions/checkout@v2")
            - ("uses" req "actions/setup-java@v1")
            indent {
              "with" block {
                "java-version" eq "1.11"
              }
            }
            - ("name" req "Cache Gradle Packages")
            indent {
              "uses" eq "actions/cache@v2"
              "with" block {
                "path" eq "~/.gradle/caches"
                "key" eq "\${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle.kts') }}"
                "restore-keys" eq "\${{ runner.os }}-gradle"
              }
            }
            - ("name" req "Assemble Gradle")
            indent {
              "run" eq "gradle assemble"
            }
          }
        }
        "publish-kotlin-images" block {
          "runs-on" eq "ubuntu-latest"
          "needs" block {
            - "assemble"
          }
          "steps" block {
            - ("uses" req "actions/checkout@v2")
            - ("uses" req "actions/setup-java@v1")
            indent {
              "with" block {
                "java-version" eq "1.11"
              }
            }
            - ("name" req "Docker Login")
            indent {
              "uses" eq "docker/login-action@v1"
              "with" block {
                "registry" eq "ghcr.io"
                "username" eq "\${{ secrets.ACTOR }}"
                "password" eq "\${{ secrets.SECRET }}"
              }
            }
            - ("name" req "Cache Gradle Packages")
            indent {
              "uses" eq "actions/cache@v2"
              "with" block {
                "path" eq "~/.gradle/caches"
                "key" eq "\${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle.kts') }}"
                "restore-keys" eq "\${{ runner.os }}-gradle"
              }
            }
            - ("name" req "Builds image and tags for github packages repo")
            indent {
              "run" eq "./gradlew dockerTagGithubPackages"
            }
            - ("name" req "Pushes image to github package repo")
            indent {
              "run" eq "./gradlew dockerPushGithubPackages --parallel"
            }
          }
        }
      }
    }

    // expect
    val expected = getFileSnapshot("GithubActionExample.yaml")
    assertEquals(expected, manifest.toString().trim())
  }
}
