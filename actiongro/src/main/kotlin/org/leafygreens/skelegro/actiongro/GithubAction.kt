package org.leafygreens.skelegro.actiongro

@GithubActionDslMarker
class GithubAction(var entities: MutableList<ActionEntity> = mutableListOf()) : YAML {
  override fun toString(): String = buildEntity {
    entities.forEach { appendLine(it.toString()) }
  }
}

fun githubAction(init: GithubAction.() -> Unit): GithubAction {
  val action = GithubAction()
  action.init()
  return action
}
