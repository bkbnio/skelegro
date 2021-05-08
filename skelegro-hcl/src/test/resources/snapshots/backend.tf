terraform {
  backend "remote" {
    organization = "bkbnio"

    workspaces {
      name = "my-amazing-workspace"
    }
  }
}
