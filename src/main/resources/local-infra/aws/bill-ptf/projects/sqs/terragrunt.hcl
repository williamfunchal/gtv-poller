inputs = {

  queue_list = {
    "adapter-isp-data-ready" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3
    }
  }
}
