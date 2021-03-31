package org.leafygreens.skelegro.terragro

import org.leafygreens.skelegro.utils.Constants.TAB
import org.leafygreens.skelegro.utils.Constants.quoted
import org.leafygreens.skelegro.utils.EntityBuilder

enum class VariableType(val value: String) {
  STRING("string"),
  BOOL("bool"),
  NUMBER("number")
}

sealed class Declaration : EntityBuilder {

  class Variable(
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

  class Resource(
    var type: String? = null,
    var name: String? = null,
    var entities: MutableList<DeclarationEntity> = mutableListOf()
  ) : Declaration() {
    override fun toString() = buildEntity {
      appendLine("resource ${quoted(type!!)} ${quoted(name!!)} {")
      entities.forEach { entity -> entity.toString().lines().forEach { appendLine("$TAB$it") } }
      appendLine("}")
    }
  }
  // provider declaration
  // terraform declaration
}

sealed class DeclarationEntity : EntityBuilder {
  class Simple<T>(
    var key: String? = null,
    var value: T? = null
  ) : DeclarationEntity() {
    override fun toString() = when(value!!) {
      is String -> "$key = ${quoted(value.toString())}"
      else -> "$key = $value"
    }
  }
  class Map<T>(
    var key: String? = null,
    var values: MutableList<DeclarationEntity.Simple<T>> = mutableListOf()
  ) : DeclarationEntity() {
    override fun toString() = buildEntity {
      appendLine("$key = {")
      values.forEach { value -> appendLine("$TAB$value") }
      appendLine("}")
    }
  }

  // set
  // object
  class Object(var name: String? = null, var values: MutableList<DeclarationEntity> = mutableListOf()) : DeclarationEntity() {
    override fun toString() = buildEntity {
      appendLine("$name {")
      values.forEach { value -> value.toString().lines().forEach { appendLine("$TAB$it") } }
      appendLine("}")
    }
  }
}
