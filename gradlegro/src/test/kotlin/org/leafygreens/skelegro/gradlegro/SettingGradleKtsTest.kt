package org.leafygreens.skelegro.gradlegro

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test
import org.leafygreens.skelegro.gradlegro.util.Helpers

internal class SettingGradleKtsTest {

  @Test
  fun `Can build a simple settings file with a root project name`() {
    // when
    val projectName = "potato"

    // do
    val result = SettingsGradleKts(projectName)

    // assert
    val expected = Helpers.getFileSnapshot("settingsGradleKts.txt")
    Truth.assertThat(result.toString()).isEqualTo(expected.trim())
  }
}
