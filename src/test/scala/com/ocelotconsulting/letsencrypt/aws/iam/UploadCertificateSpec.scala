package com.ocelotconsulting.letsencrypt.aws.iam

import com.amazonaws.services.identitymanagement.model.NoSuchEntityException
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.ocelotconsulting.letsencrypt.CertificateFile
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

/**
  * Created by Larry Anderson on 10/8/16.
  */
class UploadCertificateSpec extends FlatSpec with Matchers {
  val certName = "ocelotconsulting.com"
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  val certFile = mapper.readValue(Source.fromInputStream(getClass.getResourceAsStream("/fake_staged_cert.json")).getLines.mkString, classOf[CertificateFile])

  "a certificate uploaded" should "have correct delete and upload responses" in {
    try {
      val deleteResp = DeleteCertificate(certName)
      deleteResp.getSdkHttpMetadata.getHttpStatusCode shouldBe 200
    } catch {
      case e: NoSuchEntityException =>
        println("Didn't find cert to delete, no prob.")
    }
    val certResp = UploadCertificate(certName, s"${certFile.cert}${certFile.issuerCert}", certFile.key.privateKeyPem)
    certResp.getServerCertificateMetadata.getServerCertificateName shouldBe certName
    certResp.getSdkHttpMetadata.getHttpStatusCode shouldBe 200
  }
}
