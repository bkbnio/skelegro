# Skelegro 

## What is Skelegro 

Skelegro is a collection of Kotlin DSLs made to assist in the generation of full fledged repositories.  Currently,
they are being built on a by-need basis, and are constructed in a highly manual manner.  Ideally, in a follow up version,
this could be modified to generate DSLs from source much like the amazing https://github.com/fkorotkov/k8s-kotlin-dsl.

There are some challenges with that approach however, which is why the MVP libraries are all manually declared.  Primarily, 
the generative approach requires _something_ to generate off of.  Codified sources such as an API spec, JsonSchema, etc. 
would all suffice.  

## Modules

Currently, there are several modules that will generate an assortment of file types.

- dockergro -> Docker
- gradlegro -> Gradle
- actiongro -> GitHub Actions
- terragro -> Terraform

TODO go into each one in depth with examples
