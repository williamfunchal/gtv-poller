inputs = {
  ddb_tables = {
    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_locks" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 5
      write_capacity = 5
      hash_key       = {
        name = "lock_id"
        type = "S"
      }
    }
  }
}


