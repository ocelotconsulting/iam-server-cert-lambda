package com.ocelotconsulting.letsencrypt

import com.amazonaws.services.s3.event.S3EventNotification
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.io.Source

/**
  * Created by Larry Anderson on 10/7/16.
  */
object LetsEncryptLambdaIAM_UT {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  val mainObj = new LetsEncryptLambdaIAM()

  def produceS3Event = {
    val fileContents = Source.fromInputStream( getClass.getResourceAsStream("/s3_event.json") ).getLines.mkString
    mapper.readValue(fileContents, classOf[S3EventNotification])
  }

  def main(args: Array[String]): Unit =
    mainObj.configureIAMCert(produceS3Event)
}
