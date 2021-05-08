package io.bkbn.skelegro.docker

import com.google.common.truth.Truth.assertThat
import io.bkbn.skelegro.docker.commands.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class MultistageTest {

  @Test
  internal fun `can generate a multistage dockerfile`() = runBlocking {
    // when
    val dockerfile = simpleMultistage()

    // do
    val actual = dockerfile.toString()

    // expect
    val expected = TestData.getFileSnapshot("Multistage.Dockerfile")
    assertThat(actual).isEqualTo(expected)
  }

  private fun simpleMultistage(): IDockerfile {
    val stages = mapOf(
      "build" to "builder",
      "prod" to "prod"
    )
    return multistage {
      docker {
        FROM {
          comment = "Build Stage"
          image = "golang"
          version = "1.7.3"
          stageName = stages["build"] ?: error("where ya stage boi?")
        }
        WORKDIR { path = "/go/src/github.com/alexellis/href-counter/" }
        RUN { commands = listOf("go get -d -v golang.org/x/net/html") }
        COPY {
          from = "app.go"
          to = "."
        }
        RUN { commands = listOf("CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o app .") }
      }
      docker {
        FROM {
          comment = "Prod Stage"
          image = "alpine"
          version = "latest"
        }
        RUN { commands = listOf("apk --no-cache add ca-certificates") }
        WORKDIR { path = "/root/" }
        COPY {
          fromStage = stages["build"] ?: error("Where ya stage at boi?")
          from = "/go/src/github.com/alexellis/href-counter/app"
          to = "."
        }
        CMD { instructions = listOf("./app") }
      }
    }
  }
}
