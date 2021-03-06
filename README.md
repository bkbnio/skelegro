# Skelegro

![Maven Central](https://img.shields.io/maven-central/v/io.bkbn/skelegro-docker?style=for-the-badge)

## What is Skelegro

Skelegro is a collection of Kotlin DSLs made to assist in the generation of full-fledged repositories.  Currently,
they are being built on a by-need basis, and are constructed in a highly manual manner.  Ideally, in a follow up version,
this could be modified to generate DSLs from source much like the amazing https://github.com/fkorotkov/k8s-kotlin-dsl.

There are some challenges with that approach however, which is why the MVP libraries are all manually declared.  Primarily,
the generative approach requires _something_ to generate off of.  Codified sources such as an API spec, JsonSchema, etc.
would all suffice.

## How to install

Skelegro is published to Maven Central. 

```kotlin
repositories {
    mavenCentral()
}

dependencies {
  implementation("io.bkbn:skelegro-docker:latest.release")
  implementation("io.bkbn:skelegro-gradle:latest.release")
  implementation("io.bkbn:skelegro-hcl:latest.release")
  implementation("io.bkbn:skelegro-yml:latest.release")
}

```

## Modules

### YML

The following example maps to a test yaml file that can be found in the test resources of the `skelegro-yml` source code.

This builds a GitHub Action manifest

```kotlin
val manifest = yamlFile {
  "name" eq "Build and Deploy"
  "on" block {
    "push" block {
      "branches" block {
        - "main"
      }
    }
  }
  "env" block {
    "ACTOR" eq "\${{ github.actor }}"
    "SECRET" eq "\${{ github.secret }}"
  }
  "jobs" block {
    "assemble" block {
      "runs-on" eq "ubuntu-latest"
      "steps" block {
        - ("uses" req "actions/checkout@v2")
        - ("uses" req "actions/setup-java@v1")
        indent {
          "with" block {
            "java-version" eq "1.11"
          }
        }
        - ("name" req "Cache Gradle Packages")
        indent {
          "uses" eq "actions/cache@v2"
          "with" block {
            "path" eq "~/.gradle/caches"
            "key" eq "\${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle.kts') }}"
            "restore-keys" eq "\${{ runner.os }}-gradle"
          }
        }
        - ("name" req "Assemble Gradle")
        indent {
          "run" eq "gradle assemble"
        }
      }
    }
    "publish-kotlin-images" block {
      "runs-on" eq "ubuntu-latest"
      "needs" block {
        - "assemble"
      }
      "steps" block {
        - ("uses" req "actions/checkout@v2")
        - ("uses" req "actions/setup-java@v1")
        indent {
          "with" block {
            "java-version" eq "1.11"
          }
        }
        - ("name" req "Docker Login")
        indent {
          "uses" eq "docker/login-action@v1"
          "with" block {
            "registry" eq "ghcr.io"
            "username" eq "\${{ secrets.ACTOR }}"
            "password" eq "\${{ secrets.SECRET }}"
          }
        }
        - ("name" req "Cache Gradle Packages")
        indent {
          "uses" eq "actions/cache@v2"
          "with" block {
            "path" eq "~/.gradle/caches"
            "key" eq "\${{ runner.os }}-gradle-\${{ hashFiles('**/*.gradle.kts') }}"
            "restore-keys" eq "\${{ runner.os }}-gradle"
          }
        }
        - ("name" req "Builds image and tags for github packages repo")
        indent {
          "run" eq "./gradlew dockerTagGithubPackages"
        }
        - ("name" req "Pushes image to github package repo")
        indent {
          "run" eq "./gradlew dockerPushGithubPackages --parallel"
        }
      }
    }
  }
}
```

### Docker

The following builds a simple dockerfile that can be found in the test resources of the `skelegro-docker` module

```kotlin
docker {
  FROM {
    comment = "A basic apache server with PHP. To use either add or bind mount content under /var/www"
    image = "kstaken/apache2"
  }
  LABEL {
    labels = listOf(
      Pair("maintainer", "Kimbro Staken"),
      Pair("version", "0.1")
    )
  }
  RUN {
    commands = listOf(
      "apt-get update",
      "apt-get install -y php5 libapache2-mod-php5 php5-mysql php5-cli",
      "apt-get clean",
      "rm -rf /var/lib/apt/lists/*"
    )
    separator = " && "
  }
  CMD {
    instructions = listOf(
      "/usr/sbin/apache2",
      "-D",
      "FOREGROUND"
    )
  }
}
```

### Gradle

The following example maps (almost) one-to-one with the `build.gradle.kts` found in the root of this repository

```kotlin
val buildScript = buildGradleKts {
  "plugins" block {
    +(fn("id", "org.jetbrains.kotlin.jvm") version "1.4.32" apply false)
    +(fn("id", "io.gitlab.arturbosch.detekt") version "1.16.0-RC2" apply false)
    +(fn("id", "com.adarshr.test-logger") version "3.0.0" apply false)
  }
  `---`()
  "allprojects" block {
    "group" eq "org.leafygreens"
    "version" eq "0.0.1"
    `---`()
    "repositories" block {
      "maven" block {
        "url" eq FunctionCall("uri").withArguments("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
      }
      +fn("mavenCentral")
    }
    `---`()
    +fn("apply", NamedParameter("plugin", "org.jetbrains.kotlin.jvm"))
    +fn("apply", NamedParameter("plugin", "io.gitlab.arturbosch.detekt"))
    +fn("apply", NamedParameter("plugin", "com.adarshr.test-logger"))
    +fn("apply", NamedParameter("plugin", "java-library"))
    +fn("apply", NamedParameter("plugin", "maven-publish"))
    +fn("apply", NamedParameter("plugin", "idea"))
    `---`()
    fn("tasks.withType<Test>") block {
      +fn("useJUnitPlatform")
    }
    `---`()
    "configure<com.adarshr.gradle.testlogger.TestLoggerExtension>" block {
      "theme" eq EnumReference("com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA")
      "logLevel" eq EnumReference("LogLevel.LIFECYCLE")
      "showExceptions" eq true
      "showStackTraces" eq true
      "showFullStackTraces" eq false
      "showCauses" eq true
      "slowThreshold" eq 2000
      "showSummary" eq true
      "showSimpleNames" eq false
      "showPassed" eq true
      "showSkipped" eq true
      "showFailed" eq true
      "showStandardStreams" eq false
      "showPassedStandardStreams" eq true
      "showSkippedStandardStreams" eq true
      "showFailedStandardStreams" eq true
    }
    `---`()
    fn("tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>").plus(".configureEach") block {
      "kotlinOptions" block {
        "jvmTarget" eq "11"
      }
    }
    `---`()
    "configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>" block {
      "toolVersion" eq "1.16.0-RC2"
      "config" eq FunctionCall("files").withArguments("\${rootProject.projectDir}/detekt.yml")
      "buildUponDefaultConfig" eq true
    }
    `---`()
    "configure<PublishingExtension>" block {
      "repositories" block {
        "maven" block {
          "name" eq "GithubPackages"
          "url" eq FunctionCall("uri").withArguments("https://maven.pkg.github.com/bkbnio/skelegro")
          "credentials" block {
            "username" eq FunctionCall("System.getenv").withArguments("GITHUB_ACTOR")
            "password" eq FunctionCall("System.getenv").withArguments("GITHUB_TOKEN")
          }
        }
      }
    }
    `---`()
    "configure<JavaPluginExtension>" block {
      +fn("withSourcesJar")
    }
  }
}
```

### HCL

Let's explore using `skelegro-hcl` to generate a Terraform manifest for a kubernetes deployment

```kotlin
val manifest = hclFile {
  "variable" label "github_token" block {
    "type" eq HclType.STRING
    "sensitive" eq true
  }
  `---`()
  "resource" label "kubernetes_deployment" label "potato_app" block {
    "metadata" block {
      "name" eq FunctionCall("base64decode").withArguments(
        Reference("data", "digitalocean_kubernetes_cluster", "cluster", "kube_config", 0, "cluster_ca_certificate")
      )
      "labels" eqBlock {
        "application" eq "potato-app"
        "owner" eq "big-boss"
      }
    }
    "spec" block {
      "replicas" eq 3
      "selector" block {
        "match_labels" eqBlock {
          "application" eq "potato-app"
          "owner" eq "big-boss"
        }
      }
      "template" block {
        "metadata" block {
          "labels" eqBlock {
            "application" eq "potato-app"
            "owner" eq "big-boss"
          }
        }
        "spec" block {
          "image_pull_secrets" block {
            "name" eq "ghcr"
          }
          "container" block {
            "image" eq "my-image:latest"
            "name" eq "potato-app"
            "image_pull_policy" eq "Always"
            "port" block {
              "container_port" eq 8080
              "protocol" eq "TCP"
            }
            "env" block {
              "name" eq "MY_SPECIAL_ENV_VAR"
              "value" eq Reference("data", "vault_generic_secret", "credentials", "data[\"token\"]")
            }
            "env" block {
              "name" eq "GITHUB_TOKEN"
              "value" eq Reference("var", "github_token")
            }
            "resources" block {
              "limits" eqBlock {
                "cpu" eq "1"
                "memory" eq "1024Mi"
              }
              "requests" eqBlock {
                "cpu" eq "0.5"
                "memory" eq "512Mi"
              }
            }
            "liveness_probe" block {
              "http_get" block {
                "path" eq "/"
                "port" eq 8080
              }
              "initial_delay_seconds" eq 30
              "period_seconds" eq 30
            }
          }
        }
      }
    }
  }
}
```
