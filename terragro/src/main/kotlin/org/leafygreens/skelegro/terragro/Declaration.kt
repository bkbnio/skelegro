package org.leafygreens.skelegro.terragro

import org.leafygreens.skelegro.terragro.Constants.TAB
import org.leafygreens.skelegro.terragro.Constants.quoted

enum class VariableType(val value: String) {
  STRING("string"),
  BOOL("bool"),
  NUMBER("number")
}

sealed class Declaration : Entity {

  class Variable(
    private val name: String,
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

  abstract class NestedDeclaration(open val entities: MutableList<DeclarationEntity>) : Declaration()

  class Resource(
    private val type: String,
    private val name: String,
    override val entities: MutableList<DeclarationEntity> = mutableListOf()
  ) : NestedDeclaration(entities) {
    override fun toString() = buildEntity {
      appendLine("resource ${quoted(type)} ${quoted(name)} {")
      entities.forEach { entity -> entity.toString().lines().forEach { appendLine("$TAB$it") } }
      appendLine("}")
    }
  }

  class Provider(
    private val name: String,
    override val entities: MutableList<DeclarationEntity> = mutableListOf()
  ) : NestedDeclaration(entities) {
    override fun toString() = buildEntity {
      appendLine("provider ${quoted(name)} {")
      entities.forEach { entity -> entity.toString().lines().forEach { appendLine("$TAB$it") } }
      appendLine("}")
    }
  }

  class Terraform(
    override val entities: MutableList<DeclarationEntity> = mutableListOf()
  ) : NestedDeclaration(entities) {
    override fun toString() = buildEntity {
      appendLine("terraform {")
      entities.forEach { entity -> entity.toString().lines().forEach { appendLine("$TAB$it") } }
      appendLine("}")
    }
  }
}

object DeclarationExtensions {
  fun TerraformManifest.variableDeclaration(name: String, init: Declaration.Variable.() -> Unit): Declaration.Variable {
    val declaration = Declaration.Variable(name)
    declaration.init()
    declarations.add(declaration)
    return declaration
  }

  fun TerraformManifest.resourceDeclaration(
    type: String,
    name: String,
    init: Declaration.Resource.() -> Unit
  ): Declaration.Resource {
    val declaration = Declaration.Resource(type, name)
    declaration.init()
    declarations.add(declaration)
    return declaration
  }

  fun TerraformManifest.terraformDeclaration(init: Declaration.Terraform.() -> Unit): Declaration.Terraform {
    val declaration = Declaration.Terraform()
    declaration.init()
    declarations.add(declaration)
    return declaration
  }

  fun TerraformManifest.providerDeclaration(name: String, init: Declaration.Provider.() -> Unit): Declaration.Provider {
    val declaration = Declaration.Provider(name)
    declaration.init()
    declarations.add(declaration)
    return declaration
  }
}

sealed class DeclarationEntity : Entity {

  class Simple<T>(
    private val key: String,
    private val value: T
  ) : DeclarationEntity() {
    override fun toString() = when (value) {
      is String -> "$key = ${quoted(value.toString())}"
      else -> "$key = $value"
    }
  }

  class Map<T>(
    private val key: String,
    var values: MutableList<Simple<T>> = mutableListOf()
  ) : DeclarationEntity() {
    override fun toString() = buildEntity {
      appendLine("$key = {")
      values.forEach { value -> appendLine("$TAB$value") }
      appendLine("}")
    }
  }

  // set

  class Object(var name: String? = null, var values: MutableList<DeclarationEntity> = mutableListOf()) :
    DeclarationEntity() {
    override fun toString() = buildEntity {
      appendLine("$name {")
      values.forEach { value -> value.toString().lines().forEach { appendLine("$TAB$it") } }
      appendLine("}")
    }
  }

  class HCL(val type: String, val name: String, var values: MutableList<DeclarationEntity> = mutableListOf()) :
    DeclarationEntity() {
    override fun toString() = buildEntity {
      appendLine("$type ${quoted(name)} {")
      values.forEach { value -> value.toString().lines().forEach { appendLine("$TAB$it") } }
      append("}")
    }
  }
}

object DeclarationEntityExtensions {
  fun Declaration.NestedDeclaration.objectEntity(
    name: String,
    init: DeclarationEntity.Object.() -> Unit
  ): DeclarationEntity.Object {
    val declarationEntity = DeclarationEntity.Object(name)
    declarationEntity.init()
    entities.add(declarationEntity)
    return declarationEntity
  }

  fun Declaration.NestedDeclaration.hcl(
    type: String,
    name: String,
    init: DeclarationEntity.HCL.() -> Unit
  ): DeclarationEntity.HCL {
    val declarationEntity = DeclarationEntity.HCL(type, name)
    declarationEntity.init()
    entities.add(declarationEntity)
    return declarationEntity
  }

  fun <T> Declaration.NestedDeclaration.keyVal(key: String, value: T): DeclarationEntity.Simple<T> {
    val entity = DeclarationEntity.Simple(key, value)
    entities.add(entity)
    return entity
  }

  fun <T> DeclarationEntity.Object.keyVal(key: String, value: T): DeclarationEntity.Simple<T> {
    val entity = DeclarationEntity.Simple(key, value)
    values.add(entity)
    return entity
  }

  fun <T> DeclarationEntity.HCL.keyVal(key: String, value: T): DeclarationEntity.Simple<T> {
    val entity = DeclarationEntity.Simple(key, value)
    values.add(entity)
    return entity
  }

  fun <T> DeclarationEntity.Map<T>.keyVal(key: String, value: T): DeclarationEntity.Simple<T> {
    val entity = DeclarationEntity.Simple(key, value)
    values.add(entity)
    return entity
  }

  // This... cannot be typed 🤔 Needs to be able to take any valid type of Simple entity, cuz of references n stuff
  fun <T> DeclarationEntity.Object.entityMap(
    name: String,
    init: DeclarationEntity.Map<T>.() -> Unit
  ): DeclarationEntity.Map<T> {
    val entity = DeclarationEntity.Map<T>(name)
    entity.init()
    values.add(entity)
    return entity
  }

  // TODO Need DSL Markers
  fun DeclarationEntity.Object.objectEntity(
    name: String,
    init: DeclarationEntity.Object.() -> Unit
  ): DeclarationEntity.Object {
    val declarationEntity = DeclarationEntity.Object(name)
    declarationEntity.init()
    values.add(declarationEntity)
    return declarationEntity
  }

  fun <T> DeclarationEntity.HCL.entityMap(
    name: String,
    init: DeclarationEntity.Map<T>.() -> Unit
  ): DeclarationEntity.Map<T> {
    val entity = DeclarationEntity.Map<T>(name)
    entity.init()
    values.add(entity)
    return entity
  }

  fun DeclarationEntity.HCL.objectEntity(
    name: String,
    init: DeclarationEntity.Object.() -> Unit
  ): DeclarationEntity.Object {
    val declarationEntity = DeclarationEntity.Object(name)
    declarationEntity.init()
    values.add(declarationEntity)
    return declarationEntity
  }
}

class VariableReference(private val varName: String) {
  override fun toString() = "var.$varName"
}

class ResourceReference(private val resourcePath: String) {
  override fun toString() = resourcePath
}
