plugins {
  id("io.bkbn.sourdough.library")
}

dependencies {
  implementation(libs.kotlinpoet)
  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)
}

sourdough {
  githubOrg.set("bkbnio")
  githubRepo.set("skelegro")
  libraryName.set("Skelegro")
  libraryDescription.set("A wacky assortment of Kotlin DSLs for infrastructure manifest generation")
  licenseName.set("MIT License")
  licenseUrl.set("https://mit-license.org")
  developerId.set("bkbnio")
  developerName.set("Ryan Brink")
  developerEmail.set("admin@bkbn.io")
}
