# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.5.1] - Aprid 3rd, 2021

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
