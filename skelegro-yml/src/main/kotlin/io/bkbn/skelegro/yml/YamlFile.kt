package io.bkbn.skelegro.yml

class YamlFile {

  companion object {
    private const val TAB = "  "
  }

  private fun addIndent(level: Int): String = TAB.repeat(level)
  private val sb = StringBuilder()
  private var level = 0

  infix fun String.eq(value: Any) = sb.appendIndented("$this: $value")
  infix fun String.req(value: Any) = "$this: $value"

  infix fun String.block(block: () -> Unit): String {
    sb.appendIndented("$this:")
    level++
    block.invoke()
    level--
    return this
  }

  fun indent(block: () -> Unit) {
    level++
    block.invoke()
    level--
  }

  operator fun String.unaryMinus(): String {
    sb.appendIndented("- $this")
    return this
  }

  operator fun String.unaryPlus() {
    this.lines().forEach { sb.appendIndented(it) }
  }

  override fun toString() = sb.toString()
  private fun StringBuilder.appendIndented(value: Any) = appendLine("${addIndent(level)}$value")
}

fun yamlFile(
  init: YamlFile.() -> Unit
): YamlFile {
  val action = YamlFile()
  action.init()
  return action
}
