resource "vault_generic_secret" "my-secret" {
  data_json = <<EOF
  {
    "test": "${kubernetes_namespace.vault.count}",
  }
  EOF
  path = "important/test"
}
