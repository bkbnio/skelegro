package io.bkbn.skelegro.docker.commands

import io.bkbn.skelegro.docker.Dockerfile

private typealias LabelPair = Pair<String, String>

class Label(var labels: List<LabelPair> = emptyList(), comment: String? = null) : Command(comment) {
  override fun tag() = "LABEL"
  override fun toString() = "${tag()} ${labels.joinToString(separator = " ") { "${it.first}=\"${it.second}\"" }}"
}

fun Dockerfile.LABEL(init: Label.() -> Unit): Label {
  val label = Label().apply(init)
  steps.add(label)
  return label
}
