package io.bkbn.skelegro.docker.commands

import io.bkbn.skelegro.docker.Dockerfile

class Arg(
  var name: String = "",
  var default: String? = null,
  comment: String? = null
) : Command(comment) {
  override fun tag() = "ARG"
  override fun toString() = "${tag()} ${if (default != null) "$name=$default" else name}"
}

fun Dockerfile.ARG(init: Arg.() -> Unit): Arg {
  val arg = Arg().apply(init)
  steps.add(arg)
  return arg
}
