package com.ocelotconsulting

import java.net.URLDecoder

/**
  * Created by Larry Anderson on 10/10/16.
  */
package object letsencrypt {
  def decodeS3Key(key: String): String = URLDecoder.decode(key.replace("+", " "), "utf-8")
}
