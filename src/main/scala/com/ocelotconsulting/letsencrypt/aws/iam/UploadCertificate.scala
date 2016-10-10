package com.ocelotconsulting.letsencrypt.aws.iam

import com.amazonaws.services.identitymanagement.model.{UploadServerCertificateRequest, UploadServerCertificateResult}

/**
  * Created by Larry Anderson on 10/8/16.
  */
object UploadCertificate {
  def apply(name: String, cert: String, privateKey: String): UploadServerCertificateResult = iam.uploadServerCertificate(new UploadServerCertificateRequest(name, cert, privateKey))
}
