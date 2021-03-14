package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile

private typealias Variables = Pair<String, String>

class Env(
    var variables: List<Variables> = emptyList(),
    comment: String? = null
) : Command(comment) {
  override fun tag() = "ENV"
  override fun toString() = "${tag()} ${variables.joinToString(separator = " ") { "${it.first}=${it.second}" }}"
}

fun Dockerfile.ENV(init: Env.() -> Unit): Env {
  val env = Env().apply(init)
  steps.add(env)
  return env
}
