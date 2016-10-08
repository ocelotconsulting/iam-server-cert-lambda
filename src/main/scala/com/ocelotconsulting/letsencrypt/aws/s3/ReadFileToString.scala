package com.ocelotconsulting.letsencrypt.aws.s3

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.s3.{AmazonS3, AmazonS3Client}
import com.amazonaws.services.s3.model.{GetObjectRequest, S3Object}

import scala.io.Source

/**
  * Created by Larry Anderson on 10/8/16.
  */
object ReadFileToString {
  def apply(bucket : String, key: String): String = {
    val s3Client: AmazonS3 = new AmazonS3Client(new ProfileCredentialsProvider())
    val obj: S3Object = s3Client.getObject(new GetObjectRequest(bucket, key))
    Source.fromInputStream( obj.getObjectContent ).getLines.mkString
  }
}
