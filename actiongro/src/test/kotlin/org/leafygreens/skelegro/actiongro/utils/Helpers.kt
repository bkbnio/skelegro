package org.leafygreens.skelegro.actiongro.utils

import java.io.File

object Helpers {
  // TODO This needs to be in a test utils folder
  fun getFileSnapshot(fileName: String): String {
    val snapshotPath = "src/test/resources/snapshots"
    val file = File("$snapshotPath/$fileName")
    return file.readText().trim()
  }
}
