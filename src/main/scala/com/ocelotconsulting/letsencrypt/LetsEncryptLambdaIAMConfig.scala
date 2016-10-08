package com.ocelotconsulting.letsencrypt

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.language.postfixOps

/**
  * Created by Larry Anderson on 10/8/16.
  */
object LetsEncryptLambdaIAMConfig {
  val config = ConfigFactory.load()
  val certMap: Map[String, String] = config.getObject("cert-path-to-iam").entrySet().asScala.map { entry =>
    entry.getKey -> entry.getValue.render()
  } toMap
}
