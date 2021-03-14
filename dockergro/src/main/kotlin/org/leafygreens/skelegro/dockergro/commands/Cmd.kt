package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile

typealias Instructions = List<String>

class Cmd(
    var instructions: Instructions = emptyList(),
    comment: String? = null
) : Command(comment) {
  override fun tag() = "CMD"
  override fun toString() = "${tag()} ${instructions.joinToString(prefix = "[\"", separator = "\", \"", postfix = "\"]")}"
}

fun Dockerfile.CMD(init: Cmd.() -> Unit): Cmd {
  val cmd = Cmd().apply(init)
  steps.add(cmd)
  return cmd
}
