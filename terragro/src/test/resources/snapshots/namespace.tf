resource "kubernetes_namespace" "vault" {
  metadata {
    annotations = {
      name = "vault"
    }

    labels = {
      role = "vault"
    }

    name = "vault"
  }
}
