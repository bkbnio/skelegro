# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

As of version 0.8.0, all breaking changes are marked with 🚨

## [1.0.1] - June 19th, 2021

### Changed

- Some gradle stuff
- Codacy

## [1.0.0] - June 1st, 2021

### Added

- Code Coverage

## [1.0.0-rc] - June 1st, 2021

### Added

- Maven Central

## [1.0.0-beta] - May 8th, 2021

### Changed

- Migrated to io.bkbn
- Generalized packages

## [0.10.0] - May 7th, 2021

### Changed

- 🚨 Complete re-haul of the `TerraGro` DSL.  Same vein as previous rewrites.

## [0.9.1] - May 3rd, 2021

### Added

- Unary Plus operator for GitHub Action DSL

## [0.9.0] - May 3rd, 2021

### Changed

- 🚨 Complete re-haul of the `ActionGro` DSL.  Much like the gradle refactor, scraps the super brittle V1 for a simple, highly extensible DSL.

## [0.8.0] - May 3rd, 2021

### Changed

- 🚨 Complete re-haul of the `GradleGro` DSL.  Scrapped the highly rigid, explicit way for a highly extensible and declarative approach

## [0.7.7] - May 2nd, 2021

### Added

- Function reference type

## [0.7.6] - May 2nd, 2021

### Added

- Data Reference type

## [0.7.5] - April 29th, 2021

### Added

- Data type for Terraform DSL

## [0.7.4] - April 28th, 2021

### Added

- General HCL entity

## [0.7.3] - April 27th, 2021

### Added

- Heredoc as supported type for Terraform values
- PR Template

## [0.7.2] - April 15th, 2021

### Added

- Source code to published artifact

## [0.7.1] - April 11th, 2021

### Changed

- Microscopic touch up, want to trigger new build mostly

## [0.7.0] - April 10th, 2021

### Added

- Gradle 7 Baby 🎉 May it suck less

## [0.6.0] - April 10th, 2021

### Removed

- jcenter as an approved gradle repository

## [0.5.4] - April 10th, 2021

### Changed

- Gradle 7 RC
- Removed JCenter in favor of MavenCentral and added explicit kotlinx-html repo... this Bintray dep is gonna fuck everything

## [0.5.2] - April 3rd, 2021

### Changed

- Dummy McDumbkins over here forgot the DSL function

## [0.5.1] - April 3rd, 2021

### Changed

- Modified the spacing in gradle files to prevent missing values from adding padding whitespace

## [0.5.0] - April 3rd, 2021

### Added

- Oh dude this gradle library is _disgusting_.  Need to come up with a better solution.  Anyways, 
  I added an `allprojects` block to the gradle build file tool, so that I can setup root functionality 
  in generated projects.

## [0.4.0] - April 3rd, 2021

### Added

- `settingsGradleKts` DSL to implement `settings.gradle.kts` files in a declarative manner
- `include` functionality to declare child modules in a gradle project

## [0.3.0] - March 31st, 2021

### Removed 

- Utils module that was causing way more trouble than it was worth... gradle is a fucking dumpster fire, quote it - [@rgbrizzlehizzle](https://github.com/rgbrizzlehizzle).

## [0.2.0] - March 31st, 2021
### Added
- MVP For terraform DSL [@rgbrizzlehizzle](https://github.com/rgbrizzlehizzle).
- Changelog [@rgbrizzlehizzle](https://github.com/rgbrizzlehizzle).
- Absolutely paltry README [@rgbrizzlehizzle](https://github.com/rgbrizzlehizzle).

### Changed
- Modified the repo structure to include a utils module for cross module functionality

### Removed
- Section about "changelog" vs "CHANGELOG".
