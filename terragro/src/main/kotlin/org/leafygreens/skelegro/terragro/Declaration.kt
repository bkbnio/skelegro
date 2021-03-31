package org.leafygreens.skelegro.terragro

import org.leafygreens.skelegro.utils.Constants.TAB
import org.leafygreens.skelegro.utils.EntityBuilder

enum class VariableType(val value: String) {
  STRING("string"),
  BOOL("bool"),
  NUMBER("number")
}

sealed class Declaration : EntityBuilder {

  class VariableDeclaration(
    var name: String? = null,
    var type: VariableType? = null,
    var sensitive: Boolean = false
  ) : Declaration() {
    override fun toString() = buildEntity {
      appendLine("variable \"$name\" {")
      appendLine("${TAB}type = ${type!!.value}")
      appendLine("${TAB}sensitive = $sensitive")
      appendLine("}")
    }
  }
  // resource declaration
  // provider declaration
  // terraform declaration
}

sealed class DeclarationEntity : EntityBuilder {
  // simple (string, int, bool)
  // map
  // set
  // object
}
