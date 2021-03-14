package org.leafygreens.skelegro.dockergro

import org.leafygreens.skelegro.dockergro.commands.Command
import org.leafygreens.skelegro.dockergro.commands.From
import java.util.ArrayList

open class Dockerfile(var from: From? = null, var steps: ArrayList<Command> = arrayListOf()) : IDockerfile {

  private fun buildDockerfile(build: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.build()
    return stringBuilder.toString().trim()
  }

  override fun toString(): String {
    return buildDockerfile {
      if (!from?.comment.isNullOrBlank()) appendLine("# ${from?.comment}")
      appendLine(from)
      steps.forEach {
        if (!it.comment.isNullOrBlank()) appendLine("# ${it.comment}")
        appendLine(it.toString())
      }
    }
  }
}

fun docker(init: Dockerfile.() -> Unit): Dockerfile {
  val dockerfile = Dockerfile()
  dockerfile.init()
  return dockerfile
}
