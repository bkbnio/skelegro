plugins {
  id("io.bkbn.sourdough.library")
}

dependencies {
  implementation(libs.kotlinx.coroutines) // todo needed?
  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)
}
