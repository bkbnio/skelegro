package io.bkbn.skelegro.docker.commands

import io.bkbn.skelegro.docker.Dockerfile

typealias Instructions = List<String>

class Cmd(
    var instructions: Instructions = emptyList(),
    comment: String? = null
) : Command(comment) {
  override fun tag() = "CMD"
  override fun toString() =
    "${tag()} ${instructions.joinToString(prefix = "[\"", separator = "\", \"", postfix = "\"]")}"
}

fun Dockerfile.CMD(init: Cmd.() -> Unit): Cmd {
  val cmd = Cmd().apply(init)
  steps.add(cmd)
  return cmd
}
