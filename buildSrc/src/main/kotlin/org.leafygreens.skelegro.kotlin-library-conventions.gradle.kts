plugins {
  id("org.leafygreens.skelegro.kotlin-common-conventions")
  `java-library`
  `maven-publish`
}

java {
  withSourcesJar()
}

publishing {
  repositories {
    maven {
      name = "GithubPackages"
      url = uri("https://maven.pkg.github.com/lg-backbone/skelegro")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("skelegro") {
      from(components["kotlin"])
    }
  }
}
