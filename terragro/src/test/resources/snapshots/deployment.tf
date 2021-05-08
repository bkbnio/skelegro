variable "github_token" {
  type = string
  sensitive = true
}

resource "kubernetes_deployment" "potato_app" {
  metadata {
    name = base64decode(data.digitalocean_kubernetes_cluster.cluster.kube_config.0.cluster_ca_certificate)
    labels = {
      application = "potato-app"
      owner = "big-boss"
    }
  }
  spec {
    replicas = 3
    selector {
      match_labels = {
        application = "potato-app"
        owner = "big-boss"
      }
    }
    template {
      metadata {
        labels = {
          application = "potato-app"
          owner = "big-boss"
        }
      }
      spec {
        image_pull_secrets {
          name = "ghcr"
        }
        container {
          image = "my-image:latest"
          name = "potato-app"
          image_pull_policy = "Always"
          port {
            container_port = 8080
            protocol = "TCP"
          }
          env {
            name = "MY_SPECIAL_ENV_VAR"
            value = data.vault_generic_secret.credentials.data["token"]
          }
          env {
            name = "GITHUB_TOKEN"
            value = var.github_token
          }
          resources {
            limits = {
              cpu = "1"
              memory = "1024Mi"
            }
            requests = {
              cpu = "0.5"
              memory = "512Mi"
            }
          }
          liveness_probe {
            http_get {
              path = "/"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds = 30
          }
        }
      }
    }
  }
}
