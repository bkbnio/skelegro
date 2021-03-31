terraform {
  required_providers {
    helm = {
      source = "hashicorp/helm"
      version = "2.0.3"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "2.0.3"
    }
  }
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
    config_context = "my-cluster"
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
  config_context = "my-cluster"
}
