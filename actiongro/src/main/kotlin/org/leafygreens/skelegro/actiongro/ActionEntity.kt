package org.leafygreens.skelegro.actiongro

import org.leafygreens.skelegro.actiongro.Constants.TAB

open class ActionEntity : YAML {

  open class StringEntity(
    open var key: String? = null,
    open var value: String? = null
  ) : ActionEntity() {
    override fun toString() = buildEntity {
      appendLine("$key: $value")
    }
  }

  open class NestedEntity(
    open var key: String? = null,
    open var value: ActionEntity? = null
  ) : ActionEntity() {
    override fun toString(): String = buildEntity {
      appendLine("$key:")
      value.toString().lines().forEach {
        appendLine("$TAB$it")
      }
    }
  }

  open class NestedEntities(
    open var key: String? = null,
    open var values: MutableList<ActionEntity> = mutableListOf()
  ) : ActionEntity() {
    override fun toString(): String = buildEntity {
      appendLine("$key:")
      values.forEach { value ->
        value.toString().lines().forEach {
          appendLine("$TAB$it")
        }
      }
    }
  }

  open class ArrayEntity<T>(
    open var key: String? = null,
    open var values: MutableList<Pair<T, Boolean>> = mutableListOf()
  ) : ActionEntity() {
    override fun toString(): String = buildEntity {
      appendLine("$key:")
      values.forEach { (value, hyphenated) ->
        if (hyphenated) {
          value.toString().lines().forEach { appendLine("$TAB- $it") }
        } else {
          value.toString().lines().forEach { appendLine("$TAB  $it") }
        }
      }
    }
  }

  @GithubActionDslMarker
  class StringEntityWrapper(
    override var key: String? = null,
    override var value: String? = null
  ) : StringEntity(key, value)

  @GithubActionDslMarker
  class NestedEntityWrapper(
    override var key: String? = null,
    override var value: ActionEntity? = null
  ) : NestedEntity(key, value)

  @GithubActionDslMarker
  class NestedEntitiesWrapper(
    override var key: String? = null,
    override var values: MutableList<ActionEntity> = mutableListOf()
  ) : NestedEntities(key, values)
}
