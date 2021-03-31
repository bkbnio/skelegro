resource "kubernetes_service" "backbone_generator_service" {
  metadata {
    name = "backbone-generator"
  }
  spec {
    //    session_affinity = "ClientIP"     // TODO Look into this
    //    type             = "LoadBalancer" // TODO Look into this
    selector = {
      application = kubernetes_deployment.backbone_generator.metadata.0.labels.application
      owner       = kubernetes_deployment.backbone_generator.metadata.0.labels.owner
    }
    port {
      port        = 80
      target_port = 8080
    }
  }
}
