package io.bkbn.skelegro.docker

import java.io.File

object TestData {
  fun getFileSnapshot(fileName: String): String {
    val snapshotPath = "src/test/resources/snapshots"
    val file = File("$snapshotPath/$fileName")
    return file.readText()
  }
}
