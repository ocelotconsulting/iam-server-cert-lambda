package com.ocelotconsulting.letsencrypt

import com.typesafe.config.ConfigFactory

import scala.language.postfixOps

/**
  * Created by Larry Anderson on 10/8/16.
  */
object LetsEncryptLambdaIAMConfig {
  val config = ConfigFactory.load()
  def certMap(s3Path: String): String = config.getConfig("cert-path-to-iam").getString(s3Path)
}
