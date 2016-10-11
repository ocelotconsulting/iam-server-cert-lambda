package com.ocelotconsulting.ssl

import scala.collection.JavaConversions._
import com.amazonaws.services.identitymanagement.model.{NoSuchEntityException, UploadServerCertificateResult}
import com.amazonaws.services.lambda.runtime.events.SNSEvent
import com.amazonaws.services.s3.event.S3EventNotification
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.ocelotconsulting.ssl.aws.iam.{DeleteCertificate, UploadCertificate}
import com.ocelotconsulting.ssl.aws.s3.ReadFileToString

import scala.language.postfixOps

/**
  * Created by Larry Anderson on 10/7/16.
  */

class IAMServerCertificateLambda {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  private def upload(s3Entity: S3Entity, cert: CertificateFile): UploadServerCertificateResult = {
    val certName = IAMServerCertificateLambdaConfig.certMap(s"${decodeS3Key(s3Entity.getBucket.getName)}/${decodeS3Key(s3Entity.getObject.getKey)}")
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

  private def extractS3Event(snsRecord: SNSEvent.SNSRecord): S3EventNotification = S3EventNotification.parseJson(snsRecord.getSNS.getMessage)

  private def updateCert(s3Event: S3EventNotification): java.util.List[String] =
    s3Event.getRecords.map(_.getS3).map { s3Object => transformResult(upload(s3Object, retrieveCert(s3Object)))}

  // Actual lambda function
  def configureIAMCert(event: SNSEvent): java.util.List[String] = event.getRecords.map { extractS3Event } flatMap updateCert
}