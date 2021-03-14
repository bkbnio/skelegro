package org.leafygreens.skelegro.actiongro

object ActionEntityExtensions {
  fun GithubAction.stringEntity(init: ActionEntity.StringEntityWrapper.() -> Unit): ActionEntity.StringEntityWrapper {
    val entity = ActionEntity.StringEntityWrapper().apply(init)
    entities.add(entity)
    return entity
  }

  fun GithubAction.nestedEntity(init: ActionEntity.NestedEntityWrapper.() -> Unit): ActionEntity.NestedEntityWrapper {
    val entity = ActionEntity.NestedEntityWrapper().apply(init)
    entities.add(entity)
    return entity
  }

  fun GithubAction.nestedEntities(init: ActionEntity.NestedEntitiesWrapper.() -> Unit): ActionEntity.NestedEntitiesWrapper {
    val entity = ActionEntity.NestedEntitiesWrapper().apply(init)
    entities.add(entity)
    return entity
  }

  fun ActionEntity.NestedEntityWrapper.nestedEntity(init: ActionEntity.NestedEntity.() -> Unit): ActionEntity.NestedEntity {
    val entity = ActionEntity.NestedEntity().apply(init)
    value = entity
    return entity
  }

  fun ActionEntity.NestedEntity.stringEntity(init: ActionEntity.StringEntity.() -> Unit): ActionEntity.StringEntity {
    val entity = ActionEntity.StringEntity().apply(init)
    value = entity
    return entity
  }

  fun ActionEntity.NestedEntities.stringEntity(init: ActionEntity.StringEntity.() -> Unit): ActionEntity.StringEntity {
    val entity = ActionEntity.StringEntity().apply(init)
    values.add(entity)
    return entity
  }

  fun ActionEntity.NestedEntity.nestedEntities(init: ActionEntity.NestedEntities.() -> Unit): ActionEntity.NestedEntities {
    val entities = ActionEntity.NestedEntities().apply(init)
    value = entities
    return entities
  }

  fun ActionEntity.NestedEntitiesWrapper.stringEntity(init: ActionEntity.StringEntity.() -> Unit): ActionEntity.StringEntity {
    val entity = ActionEntity.StringEntity().apply(init)
    values.add(entity)
    return entity
  }

  fun <T> ActionEntity.NestedEntities.arrayEntity(init: ActionEntity.ArrayEntity<T>.() -> Unit): ActionEntity.ArrayEntity<T> {
    val entity = ActionEntity.ArrayEntity<T>().apply(init)
    values.add(entity)
    return entity
  }

  fun <T> ActionEntity.NestedEntity.arrayEntity(init: ActionEntity.ArrayEntity<T>.() -> Unit): ActionEntity.ArrayEntity<T> {
    val entity = ActionEntity.ArrayEntity<T>().apply(init)
    value = entity
    return entity
  }



}
