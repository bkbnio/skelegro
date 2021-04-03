# Skelegro 

## What is Skelegro 

Skelegro is a collection of Kotlin DSLs made to assist in the generation of full fledged repositories.  Currently,
they are being built on a by-need basis, and are constructed in a highly manual manner.  Ideally, in a follow up version,
this could be modified to generate DSLs from source much like the amazing https://github.com/fkorotkov/k8s-kotlin-dsl.

There are some challenges with that approach however, which is why the MVP libraries are all manually declared.  Primarily, 
the generative approach requires _something_ to generate off of.  Codified sources such as an API spec, JsonSchema, etc. 
would all suffice.  

## Modules

### ActionGro

TODO

### DockerGro

TODO

### GradleGro

#### Growing a settings kotlin script

The `settings.gradle.kts` file is relatively bare-bone, and is predominantly used
to declare the top level settings for a gradle-based repository.  Right now in skelegro,
functionality is limited to simply declaring a root project name and adding any nested child modules

```kotlin
val result = settingsGradleKts("starchtopia") {
  include("potatoville")
  include("pastaland")
  include("ricetown")
  include("new-breadswick")
}
```

Would result in a file

```kotlin
rootProject.name = "starchtopia"
include("potatoville")
include("pastaland")
include("ricetown")
include("new-breadswick")
```

#### Growing a build kotlin script

TODO

### TerraGro

TODO
