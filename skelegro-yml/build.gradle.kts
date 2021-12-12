plugins {
  id("io.bkbn.sourdough.library")
}

dependencies {
  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)
}
