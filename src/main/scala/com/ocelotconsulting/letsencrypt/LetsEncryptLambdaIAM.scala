package com.ocelotconsulting.letsencrypt

import scala.collection.JavaConverters._
import java.net.URLDecoder

import com.amazonaws.{AmazonWebServiceResult, ResponseMetadata}
import com.amazonaws.services.s3.event.S3EventNotification
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.ocelotconsulting.letsencrypt.aws.iam.{DeleteCertificate, UploadCertificate}
import com.ocelotconsulting.letsencrypt.aws.s3.ReadFileToString

import scala.language.postfixOps

/**
  * Created by Larry Anderson on 10/7/16.
  */

class LetsEncryptLambdaIAM {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def upload(s3Entity: S3Entity, cert: CertificateFile): AmazonWebServiceResult[ResponseMetadata] = {
    val certName = LetsEncryptLambdaIAMConfig.certMap(s"${decodeS3Key(s3Entity.getBucket.getName)}/${decodeS3Key(s3Entity.getObject.getKey)}")
    try {
      DeleteCertificate(certName)
    }
    finally {
      UploadCertificate(certName, s"${cert.cert}${cert.issuerCert}", cert.key.privateKeyPem)
    }
  }

  def getCertFileAsString(s3Entity: S3Entity): String = ReadFileToString(decodeS3Key(s3Entity.getBucket.getName), decodeS3Key(s3Entity.getObject.getKey))

  def retrieveCert(s3Entity: S3Entity): CertificateFile = mapper.readValue(getCertFileAsString(s3Entity), classOf[CertificateFile])

  def decodeS3Key(key: String): String = URLDecoder.decode(key.replace("+", " "), "utf-8")

  def configureIAMCert(event: S3EventNotification): java.util.List[String] = {
    val objects = event.getRecords.asScala.map { _.getS3 }
    val s3Object = objects.toList
    println(s3Object)
    val result = upload(s3Object.head, retrieveCert(s3Object.head))
    List(result.toString) asJava
  }
}