inputs = {

  project_parameters = {
    gtv-poller = {
        "s3/integration-bucket-name"           = "${ccsi-local.aws.env}-${ccsi-local.aws.namespaces}-isp-gtv-integration-${ccsi-local.aws.regionCode}"
        "s3/integration-customer-prefix"       = "ISPPOWER.ISPCUSTOMER"
        "s3/integration-service-prefix"        = "ISPPOWER.ISPSERVICE"
        "s3/integration-corp-profile-prefix"   = "ISPPOWER.J2_CORP_PROFILE"
        "config/usage/enabled"                 = "false"
        "config/usage/batch_size"              = "1000"
        "config/usage/trigger/period"          = "30"
        "config/usage/trigger/time_unit"       = "SECONDS"
        "config/customer/enabled"              = "false"
        "config/customer/batch_size"           = "1000"
        "config/customer/trigger/period"       = "30"
        "config/customer/trigger/time_unit"    = "SECONDS"
        "config/service/enabled"               = "false"
        "config/service/batch_size"            = "1000"
        "config/service/trigger/period"        = "30"
        "config/service/trigger/time_unit"     = "SECONDS"
        "config/corp-profile/enabled"          = "false"
        "config/corp-profile/batch_size"       = "1000"
        "config/corp-profile/trigger/period"   = "30"
        "config/corp-profile/trigger/time_unit"= "SECONDS"

        "config/lock/lease_duration"           = "15"
        "config/lock/heartbeat_period"         = "3"
    }
  }

  insecure_project_parameters = {

  }

  namespace_parameters = {
    "graceful-shutdown-timeout"          = "30s"
    "sqs/adapter-isp-data-ready-url"     = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-isp-data-ready.fifo"
    "sqs/message-visibility-timeout-sec" = "60"
    "sqs/message-wait-time-sec"          = "20"
    "sqs/fetch-size"                     = "10"
    "sqs/empty-timeout-mills"            = "60000"
    "coredb/url"                         = "jdbc:oracle:thin:@coredev1-serv-usw2.czoqxvus1z1f.us-west-2.rds.amazonaws.com:1521:SERV"
    "coredb/username"                    = "gtvadapter"
  }

  durable_project_parameters_local_values = {
    gtv-poller = {
        "coredb/password" = "changeme"
    }
  }
}