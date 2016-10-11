package com.ocelotconsulting

import java.net.URLDecoder

import com.typesafe.config.ConfigFactory

/**
  * Created by Larry Anderson on 10/10/16.
  */
package object ssl {
  object IAMServerCertificateLambdaConfig {
    val config = ConfigFactory.load()
    def certMap(s3Path: String): String = config.getConfig("cert-path-to-iam").getString(s3Path)
  }

  def decodeS3Key(key: String): String = URLDecoder.decode(key.replace("+", " "), "utf-8")
}
