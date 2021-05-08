package io.bkbn.skelegro.gradle

import io.bkbn.skelegro.gradle.utils.FunctionCall

class BuildGradleKts {

  companion object {
    private const val TAB = "  "
  }

  private fun addIndent(level: Int): String = TAB.repeat(level)

  private val sb = StringBuilder()
  private var level = 0

  infix fun String.eq(value: Any) = when (value) {
    is String -> sb.appendIndented("$this = \"$value\"")
    else -> sb.appendIndented("$this = $value")
  }

  infix fun String.block(block: () -> Unit): String {
    sb.appendIndented("$this {")
    level++
    block.invoke()
    level--
    sb.appendIndented("}")
    return this
  }

  operator fun String.unaryPlus() {
    sb.appendIndented(this)
  }

  fun `---`() {
    sb.appendLine()
  }

  infix fun String.version(v: String) = "$this version \"$v\""

  infix fun String.apply(a: Boolean) = "$this apply $a"

  fun fn(name: String, vararg args: Any): String = FunctionCall(name).withArguments(*args).toString()

  override fun toString() = sb.toString()

  private fun StringBuilder.appendIndented(value: Any) = appendLine("${addIndent(level)}$value")
}

fun buildGradleKts(
  init: BuildGradleKts.() -> Unit
): BuildGradleKts {
  val bgk = BuildGradleKts()
  bgk.init()
  return bgk
}
