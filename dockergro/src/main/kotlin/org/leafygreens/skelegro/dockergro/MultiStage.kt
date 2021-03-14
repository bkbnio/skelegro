package org.leafygreens.skelegro.dockergro

class Multistage(var stages: ArrayList<Dockerfile> = arrayListOf()) : IDockerfile {

  private fun multistageBuilder(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }

  override fun toString(): String {
    return multistageBuilder {
      stages.forEach { dockerfile ->
        appendLine(dockerfile.toString())
      }
    }
  }
}

fun multistage(init: Multistage.() -> Unit): Multistage {
  return Multistage().apply(init)
}

fun Multistage.docker(init: Dockerfile.() -> Unit): Dockerfile {
  val dockerfile = Dockerfile().apply(init)
  stages.add(dockerfile)
  return dockerfile
}
