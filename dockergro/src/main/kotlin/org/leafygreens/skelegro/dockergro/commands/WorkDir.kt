package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile

class WorkDir(
  var path: String = "",
  comment: String? = null
) : Command(comment) {
  override fun tag() = "WORKDIR"
  override fun toString() = "${tag()} $path"
}

fun Dockerfile.WORKDIR(init: WorkDir.() -> Unit): WorkDir {
  val workdir = WorkDir().apply(init)
  steps.add(workdir)
  return workdir
}
