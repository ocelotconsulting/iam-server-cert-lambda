package com.ocelotconsulting.letsencrypt

import com.amazonaws.services.s3.event.S3EventNotification
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.scalatest.{FlatSpec, Matchers}
import scala.collection.JavaConverters._

import scala.io.Source

/**
  * Created by Larry Anderson on 10/7/16.
  */

class LetsEncryptLambdaIAMFromString(certFile: String) extends LetsEncryptLambdaIAM {
  override def getCertFileAsString(s3Entity: S3Entity): String = certFile
}

class LetsEncryptLambdaIAMSpec  extends FlatSpec with Matchers {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  val mainObj = new LetsEncryptLambdaIAMFromString(Source.fromInputStream(getClass.getResourceAsStream("/fake_staged_cert.json")).getLines.mkString)

  def produceS3Event: S3EventNotification =
    mapper.readValue(Source.fromInputStream( getClass.getResourceAsStream("/s3_event.json") ).getLines.mkString, classOf[S3EventNotification])

  "An S3 event" should "run lambda successfully" in {
    val something = mainObj.configureIAMCert(produceS3Event)
    something.asScala.head shouldBe "Successfully uploaded certificate for ocelotconsulting.com."
  }
}
