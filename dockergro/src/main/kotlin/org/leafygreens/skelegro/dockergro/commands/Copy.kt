package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile
import org.leafygreens.skelegro.dockergro.modify

open class Copy(
  var fromStage: String? = null,
  var from: String = ".",
  var to: String = ".",
  comment: String? = null
) : Command(comment) {
  override fun tag() = "COPY"
//  override fun toString() = "${tag()} ${fromStage.isNotBlank().let { "--from=$fromStage " }}$from $to"
  override fun toString() = "${tag()} ${fromStage?.modify { fs -> "--from=$fs " } ?: ""}$from $to"
}

fun Dockerfile.COPY(init: Copy.() -> Unit): Copy {
  val copy = Copy().apply(init)
  steps.add(copy)
  return copy
}
