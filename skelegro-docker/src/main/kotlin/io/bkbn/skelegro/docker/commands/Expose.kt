package io.bkbn.skelegro.docker.commands

import io.bkbn.skelegro.docker.Dockerfile

class Expose(
  var port: Int = 0,
  comment: String? = null
) : Command(comment) {
  override fun tag() = "EXPOSE"
  override fun toString() = "${tag()} $port"
}

fun Dockerfile.EXPOSE(init: Expose.() -> Unit): Expose {
  val expose = Expose().apply(init)
  steps.add(expose)
  return expose
}
