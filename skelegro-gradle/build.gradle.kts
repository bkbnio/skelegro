dependencies {
  // Align versions of all Kotlin components
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinpoet)

  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)

  detektPlugins(libs.detekt.formatting)
}

publishing {
  publications.withType<MavenPublication>().configureEach {
    from(components["kotlin"])
    artifact(tasks.sourcesJar)
    artifact(tasks.javadocJar)
  }
}
