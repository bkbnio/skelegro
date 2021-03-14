package org.leafygreens.skelegro.gradlegro.blocks

import org.leafygreens.skelegro.gradlegro.BuildGradleKts
import org.leafygreens.skelegro.gradlegro.utils.Helpers.blockBuilder

class TaskBlock(
  var taskTypes: MutableList<TaskTypeBlock> = mutableListOf()
) {
  override fun toString() = blockBuilder {
    appendLine("tasks {")
    taskTypes.forEach { type -> type.toString().lines().forEach { appendLine("  $it") } }
    appendLine("}")
  }
}

fun BuildGradleKts.tasks(init: TaskBlock.() -> Unit): TaskBlock {
  val tb = TaskBlock().apply(init)
  tasks = tb
  return tb
}
