package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile

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
