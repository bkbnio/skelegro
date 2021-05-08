package io.bkbn.skelegro.hcl

class HclFile {

  companion object {
    private const val TAB = "  "
  }

  private val sb = StringBuilder()
  private var level = 0

  infix fun String.eq(value: Any) = when (value) {
    is String -> sb.appendIndented("$this = \"$value\"")
    else -> sb.appendIndented("$this = $value")
  }

  infix fun String.label(value: String) = "$this \"$value\""

  infix fun String.block(block: () -> Unit): String {
    sb.appendIndented("$this {")
    level++
    block.invoke()
    level--
    sb.appendIndented("}")
    return this
  }

  infix fun String.eqBlock(block: () -> Unit): String {
    sb.appendIndented("$this = {")
    level++
    block.invoke()
    level--
    sb.appendIndented("}")
    return this
  }

  fun `---`() {
    sb.appendLine()
  }

  override fun toString() = sb.toString()

  private fun addIndent(level: Int): String = TAB.repeat(level)
  private fun StringBuilder.appendIndented(value: Any) = appendLine("${addIndent(level)}$value")
}

fun hclFile(init: HclFile.() -> Unit): HclFile {
  val manifest = HclFile()
  manifest.init()
  return manifest
}
