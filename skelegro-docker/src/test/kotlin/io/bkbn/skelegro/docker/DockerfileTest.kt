package io.bkbn.skelegro.docker

import com.google.common.truth.Truth.assertThat
import io.bkbn.skelegro.docker.commands.ARG
import io.bkbn.skelegro.docker.commands.CMD
import io.bkbn.skelegro.docker.commands.ENV
import io.bkbn.skelegro.docker.commands.EXPOSE
import io.bkbn.skelegro.docker.commands.FROM
import io.bkbn.skelegro.docker.commands.LABEL
import io.bkbn.skelegro.docker.commands.RUN
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class DockerfileTest {

  @Test
  internal fun `Can generate a sample Apache PHP Dockerfile`() = runBlocking {
    // when
    val dockerfile = sampleApacheDockerfile()

    // do
    val actual = dockerfile.toString()

    // expect
    val expected = TestData.getFileSnapshot("ApachePHP.Dockerfile")
    assertThat(actual).isEqualTo(expected)
  }

  @Test
  internal fun `Can generate a sample Apache Dockerfile`() = runBlocking {
    // when
    val dockerfile = docker {
      FROM {
        comment = "A basic apache server. To use either add or bind mount content under /var/www"
        image = "ubuntu"
        version = "12.04"
      }
      LABEL {
        labels = listOf(
          Pair("maintainer", "Kimbro Staken"),
          Pair("version", "0.1")
        )
      }
      ARG {
        name = "FIRST"
      }
      ARG {
        name = "SECOND"
        default = "THIRD"
      }
      RUN {
        commands = listOf(
          "apt-get update",
          "apt-get install -y apache2",
          "apt-get clean",
          "rm -rf /var/lib/apt/lists/*"
        )
        separator = " && "
      }
      ENV {
        variables = listOf(Pair("APACHE_RUN_USER", "www-data"))
      }
      ENV {
        variables = listOf(Pair("APACHE_RUN_GROUP", "www-data"))
      }
      ENV {
        variables = listOf(Pair("APACHE_LOG_DIR", "/var/log/apache2"))
      }
      EXPOSE {
        port = 80
      }
      CMD {
        instructions = listOf(
          "/usr/sbin/apache2",
          "-D",
          "FOREGROUND"
        )
      }
    }
    // do
    val actual = dockerfile.toString()
    // expect
    val expected = TestData.getFileSnapshot("Apache.Dockerfile")
    assertThat(actual).isEqualTo(expected.trim())
  }

  @Test
  internal fun `Can write directly to a file`() {
    // when
    val dockerfile = sampleApacheDockerfile()
    val file = java.io.File.createTempFile("abc", "def")

    // do
    dockerfile.writeTo(file)

    // expect
    assertEquals(dockerfile.toString(), file.readText())
  }

  private fun sampleApacheDockerfile() = docker {
    FROM {
      comment = "A basic apache server with PHP. To use either add or bind mount content under /var/www"
      image = "kstaken/apache2"
    }
    LABEL {
      labels = listOf(
        Pair("maintainer", "Kimbro Staken"),
        Pair("version", "0.1")
      )
    }
    RUN {
      commands = listOf(
        "apt-get update",
        "apt-get install -y php5 libapache2-mod-php5 php5-mysql php5-cli",
        "apt-get clean",
        "rm -rf /var/lib/apt/lists/*"
      )
      separator = " && "
    }
    CMD {
      instructions = listOf(
        "/usr/sbin/apache2",
        "-D",
        "FOREGROUND"
      )
    }
  }
}
