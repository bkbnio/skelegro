plugins {
  id("io.bkbn.sourdough.library")
  signing
}

dependencies {
  implementation(libs.kotlinx.coroutines) // todo needed?
  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)
}

signing {
  val signingKey: String? by project
  val signingPassword: String? by project
  useInMemoryPgpKeys(signingKey, signingPassword)
  sign(publishing.publications)
}
