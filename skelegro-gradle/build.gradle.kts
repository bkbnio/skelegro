plugins {
  id("io.bkbn.sourdough.library")
}

dependencies {
  implementation(libs.kotlinpoet)
  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)
}
