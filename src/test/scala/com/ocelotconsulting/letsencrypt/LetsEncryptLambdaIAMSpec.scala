package com.ocelotconsulting.letsencrypt

import com.amazonaws.services.s3.event.S3EventNotification
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.io.Source

/**
  * Created by Larry Anderson on 10/7/16.
  */

class LetsEncryptLambdaIAMFromString(certFile: String) extends LetsEncryptLambdaIAM {
  override def getCertFileAsString(s3Entity: S3Entity): String = certFile
}

object LetsEncryptLambdaIAMSpec {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  val mainObj = new LetsEncryptLambdaIAMFromString(Source.fromInputStream(getClass.getResourceAsStream("/staged_cert.json")).getLines.mkString)

  def produceS3Event = {
    val fileContents = Source.fromInputStream( getClass.getResourceAsStream("/s3_event.json") ).getLines.mkString
    mapper.readValue(fileContents, classOf[S3EventNotification])
  }

  def main(args: Array[String]): Unit =
    mainObj.configureIAMCert(produceS3Event)
}
