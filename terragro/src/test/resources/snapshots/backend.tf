terraform {
  backend "remote" {
    organization = "lg-backbone"
    workspaces {
      name = "my-amazing-workspace"
    }
  }
}
