package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile
import org.leafygreens.skelegro.dockergro.modify

open class From(
  var stageName: String? = null,
  var image: String = "",
  var version: String = "latest",
  comment: String? = null
) : Command(comment) {
  override fun tag() = "FROM"
  override fun toString() = "${tag()} $image:$version${stageName?.modify { sn -> " AS $sn" } ?: ""}"
}

fun Dockerfile.FROM(init: From.() -> Unit): From {
  val f = From().apply(init)
  from = f
  return f
}
