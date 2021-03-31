variable "github_token" {
  type      = string
  sensitive = true
}

// TODO Bump labels to a constant and reference??
resource "kubernetes_deployment" "backbone_generator" {
  metadata {
    name = "backbone-generator"
    labels = {
      application = "backbone-generator"
      owner       = "leafygreens-backbone"
    }
  }

  spec {
    replicas = 3

    selector {
      match_labels = {
        application = "backbone-generator"
        owner = "leafygreens-backbone"
      }
    }


    template {
      metadata {
        labels = {
          application = "backbone-generator"
          owner = "leafygreens-backbone"
        }
      }


      spec {

        image_pull_secrets {
          name = "ghcr"
        }

        container {
          image = "ghcr.io/lg-backbone/backbone-cortex-generator:0.7.0-snapshot"
          name  = "backbone-generator"

          image_pull_policy = "Always"

          port {
            container_port = 8080
            protocol = "TCP"
          }

          env {
            name  = "MONGO_USER"
            value = "generator"
          }

          env {
            name  = "MONGO_PASSWORD"
            value = "todo"
          }

          env {
            name  = "MONGO_HOST"
            value = "backbone-generator-mongo-mongodb"
          }

          env {
            name  = "GITHUB_TOKEN"
            value = var.github_token
          }

          resources {
            limits = {
              cpu    = "1"
              memory = "1024Mi"
            }
            requests = {
              cpu    = "0.5"
              memory = "512Mi"
            }
          }

          liveness_probe {
            http_get {
              path = "/"
              port = 8080
            }

            initial_delay_seconds = 30
            period_seconds        = 30
          }
        }
      }
    }
  }
}
