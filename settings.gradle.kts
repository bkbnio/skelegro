rootProject.name = "skelegro"
include("skelegro-yml")
include("skelegro-hcl")
include("skelegro-gradle")
include("skelegro-docker")

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenLocal()
  }
}
