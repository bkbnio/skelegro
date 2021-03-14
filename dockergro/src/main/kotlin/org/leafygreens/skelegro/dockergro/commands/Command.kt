package org.leafygreens.skelegro.dockergro.commands

abstract class Command(var comment: String?) {
  abstract fun tag(): String
}
