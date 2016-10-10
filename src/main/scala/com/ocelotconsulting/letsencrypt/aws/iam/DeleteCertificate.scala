package com.ocelotconsulting.letsencrypt.aws.iam

import com.amazonaws.services.identitymanagement.model.{DeleteServerCertificateRequest, DeleteServerCertificateResult}

/**
  * Created by Larry Anderson on 10/8/16.
  */
object DeleteCertificate {
  def apply(name: String): DeleteServerCertificateResult = iam.deleteServerCertificate(new DeleteServerCertificateRequest(name))
}
