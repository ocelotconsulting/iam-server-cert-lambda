package com.ocelotconsulting.letsencrypt.aws.s3

import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source

/**
  * Created by Larry Anderson on 10/8/16.
  */
class ReadFileToStringSpec extends FlatSpec with Matchers {

  val localStr = Source.fromInputStream(getClass.getResourceAsStream("/certfile.json")).getLines.mkString

  "A file read from S3" should "equal an expected string" in {
    val str =  ReadFileToString("letsencrypt-testlarry-config", "letsencrypt/certfile.json")
    str shouldBe localStr
  }

}