dependencies {
  // Align versions of all Kotlin components
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(libs.kotlinx.coroutines)

  testImplementation(libs.bundles.test.impl)
  testRuntime(libs.bundles.test.run)

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.16.0-RC2")
}

publishing {
  publications {
    create<MavenPublication>("docker") {
      from(components["kotlin"])
      artifact(tasks.sourcesJar)
    }
  }
}
