package io.bkbn.skelegro.docker.commands

import io.bkbn.skelegro.docker.Dockerfile

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
