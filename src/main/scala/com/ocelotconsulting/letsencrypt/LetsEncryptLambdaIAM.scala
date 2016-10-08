package com.ocelotconsulting.letsencrypt

import scala.collection.JavaConverters._
import java.net.URLDecoder

import com.amazonaws.services.s3.event.S3EventNotification

/**
  * Created by Larry Anderson on 10/7/16.
  */

class LetsEncryptLambdaIAM {
  def decodeS3Key(key: String): String = URLDecoder.decode(key.replace("+", " "), "utf-8")

  def configureIAMCert(event: S3EventNotification): java.util.List[String] = {
    val objects = event.getRecords.asScala.map { notification =>
      val s3 = notification.getS3
      s"${decodeS3Key(s3.getBucket.getName)}/${decodeS3Key(s3.getObject.getKey)}"
    }
    val s3Object = objects.toList
    println(s3Object)
    println(LetsEncryptLambdaIAMConfig.certMap(s3Object.head))
    objects.asJava
  }
}