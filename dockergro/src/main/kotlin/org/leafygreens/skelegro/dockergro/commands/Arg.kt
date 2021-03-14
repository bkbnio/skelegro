package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile

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
