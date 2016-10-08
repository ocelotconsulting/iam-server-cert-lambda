package com.ocelotconsulting.letsencrypt.aws.iam

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.{DeleteServerCertificateRequest, DeleteServerCertificateResult, UploadServerCertificateRequest, UploadServerCertificateResult}

/**
  * Created by Larry Anderson on 10/8/16.
  */
object DeleteCertificate {
  def apply(name: String): DeleteServerCertificateResult = {
    val iam: AmazonIdentityManagementClient = new AmazonIdentityManagementClient()
    iam.deleteServerCertificate(new DeleteServerCertificateRequest(name))
  }
}
