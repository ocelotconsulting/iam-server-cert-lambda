package com.ocelotconsulting.letsencrypt.aws.s3

/**
  * Created by Larry Anderson on 10/8/16.
  */
object ReadFileToString_UT {

  def main(args: Array[String]): Unit =
    println(ReadFileToString("letsencrypt-testlarry-certs", "letsencrypt/ocelotconsulting.com.json"))
}