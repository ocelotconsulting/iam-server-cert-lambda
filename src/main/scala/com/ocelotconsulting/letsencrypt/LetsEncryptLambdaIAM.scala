package com.ocelotconsulting.letsencrypt

import scala.collection.JavaConverters._
import com.amazonaws.services.identitymanagement.model.{NoSuchEntityException, UploadServerCertificateResult}
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

  private def upload(s3Entity: S3Entity, cert: CertificateFile): UploadServerCertificateResult = {
    val certName = LetsEncryptLambdaIAMConfig.certMap(s"${decodeS3Key(s3Entity.getBucket.getName)}/${decodeS3Key(s3Entity.getObject.getKey)}")
    try {
      DeleteCertificate(certName)
    } catch {
      case e: NoSuchEntityException =>
        println("Didn't find cert to delete, no prob.")
    }
    UploadCertificate(certName, s"${cert.cert}${cert.issuerCert}", cert.key.privateKeyPem)
  }

  protected def getCertFileAsString(s3Entity: S3Entity): String = ReadFileToString(decodeS3Key(s3Entity.getBucket.getName), decodeS3Key(s3Entity.getObject.getKey))

  private def retrieveCert(s3Entity: S3Entity): CertificateFile = mapper.readValue(getCertFileAsString(s3Entity), classOf[CertificateFile])

  private def transformResult(result: UploadServerCertificateResult) : String = s"Successfully uploaded certificate for ${result.getServerCertificateMetadata.getServerCertificateName}."

  def configureIAMCert(event: S3EventNotification): java.util.List[String] = {
    val objects = event.getRecords.asScala.map(_.getS3).toList
    val result = transformResult(upload(objects.head, retrieveCert(objects.head)))
    List(result) asJava
  }
}