package io.bkbn.skelegro.docker.commands

import io.bkbn.skelegro.docker.Dockerfile

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
