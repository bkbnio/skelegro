package org.leafygreens.skelegro.dockergro.commands

import org.leafygreens.skelegro.dockergro.Dockerfile

class User(
  var name: String = "",
  comment: String? = null
) : Command(comment) {
  override fun tag() = "USER"
  override fun toString() = "${tag()} $name"
}

fun Dockerfile.USER(init: User.() -> Unit): User {
  val user = User().apply(init)
  steps.add(user)
  return user
}
