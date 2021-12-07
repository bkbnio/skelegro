plugins {
  id("io.bkbn.sourdough.library")
}

sourdough {
  githubOrg.set("bkbnio")
  githubRepo.set("kompendium")
  githubUsername.set(System.getenv("GITHUB_ACTOR"))
  githubToken.set(System.getenv("GITHUB_TOKEN"))
  libraryName.set("Kompendium")
  libraryDescription.set("A minimally invasive OpenAPI spec generator for Ktor")
  licenseName.set("MIT License")
  licenseUrl.set("https://mit-license.org/")
  developerId.set("bkbnio")
  developerName.set("Ryan Brink")
  developerEmail.set("admin@bkbn.io")
}

dependencies {
  implementation(libs.kotlinx.coroutines) // todo needed?
  testImplementation(libs.bundles.test.impl)
  testImplementation(libs.bundles.test.run)
}

