package io.bkbn.skelegro.docker.commands

abstract class Command(var comment: String?) {
  abstract fun tag(): String
}
