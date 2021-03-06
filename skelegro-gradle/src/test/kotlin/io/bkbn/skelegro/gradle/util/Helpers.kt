package io.bkbn.skelegro.gradle.util

import java.io.File

object Helpers {
  fun getFileSnapshot(fileName: String): String {
    val snapshotPath = "src/test/resources/snapshots"
    val file = File("$snapshotPath/$fileName")
    return file.readText().trim()
  }
}
