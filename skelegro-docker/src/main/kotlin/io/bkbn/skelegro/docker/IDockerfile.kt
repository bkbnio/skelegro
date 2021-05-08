package io.bkbn.skelegro.docker

import java.io.File

interface IDockerfile {
  fun writeTo(file: File): File {
    val stringify = this.toString()
    file.writeText(stringify)
    return file
  }
}
