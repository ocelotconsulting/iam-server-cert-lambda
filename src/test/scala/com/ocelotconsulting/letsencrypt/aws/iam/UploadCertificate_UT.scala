package com.ocelotconsulting.letsencrypt.aws.iam

import com.amazonaws.services.identitymanagement.model.NoSuchEntityException
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.ocelotconsulting.letsencrypt.CertificateFile

import scala.io.Source

/**
  * Created by Larry Anderson on 10/8/16.
  */
object UploadCertificate_UT {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def certFile = {
    val fileContents = Source.fromInputStream(getClass.getResourceAsStream("/staged_cert.json")).getLines.mkString
    mapper.readValue(fileContents, classOf[CertificateFile])
  }

  def main(args: Array[String]): Unit = {
    try {
      DeleteCertificate("ocelotconsulting.com")
    } catch {
      case e: NoSuchEntityException =>
        println("Didn't find cert to delete, no prob.")
    }
    finally {
      val certResp = UploadCertificate("ocelotconsulting.com", s"${certFile.cert}${certFile.issuerCert}", certFile.key.privateKeyPem)
      println(certResp)
    }
  }
}
