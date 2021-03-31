resource "kubernetes_service" "my_service" {
  metadata {
    name = "my-service"
  }
  spec {
    selector = {
      application = kubernetes_deployment.my_app.metadata.0.labels.application
      owner = kubernetes_deployment.my_app.metadata.0.labels.owner
    }
    port {
      port = 80
      target_port = 8080
    }
  }
}
