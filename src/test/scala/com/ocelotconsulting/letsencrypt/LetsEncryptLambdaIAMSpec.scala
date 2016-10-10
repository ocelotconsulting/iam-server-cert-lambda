package com.ocelotconsulting.letsencrypt

import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.lambda.runtime.events.SNSEvent.{SNS, SNSRecord}

import scala.collection.JavaConversions._
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
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

  def produceS3Event: JsonNode =
    mapper.readValue(Source.fromInputStream( getClass.getResourceAsStream("/sns_s3_event.json") ).getLines.mkString, classOf[JsonNode])

  def toEvent: SNSEvent = {
    val event = new SNSEvent()
    val record = new SNSRecord()
    val sns = new SNS()
    val map = produceS3Event
    sns.setMessage(map.path("Records").get(0).path("Sns").path("Message").asText())
    record.setSns(sns)
    event.setRecords(List[SNSRecord](record))
    event
  }

  "An S3 event" should "run lambda successfully" in {
    val something = mainObj.configureIAMCert(toEvent)
    something.asScala.head shouldBe "Successfully uploaded certificate for ocelotconsulting.com."
  }
}
