package org.leafygreens.skelegro.terragro.utils

class FunctionCall(private val name: String) {

  var arguments: List<Any> = emptyList()

  fun withArguments(vararg args: Any): FunctionCall {
    arguments = args.toList()
    return this
  }

  override fun toString(): String {
    val guts = arguments.joinToString(", ") { arg ->
      when (arg) {
        is String -> "\"$arg\""
        else -> arg.toString()
      }
    }
    return "$name($guts)"
  }
}
