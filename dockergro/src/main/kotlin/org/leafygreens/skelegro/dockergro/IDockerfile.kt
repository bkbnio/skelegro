package org.leafygreens.skelegro.dockergro

import java.io.File

interface IDockerfile {
  fun writeTo(file: File): File {
    val stringify = this.toString()
    file.writeText(stringify)
    return file
  }
}
