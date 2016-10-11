package com.ocelotconsulting.ssl.aws

import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient

/**
  * Created by Larry Anderson on 10/10/16.
  */
package object iam {
  val iam: AmazonIdentityManagementClient = new AmazonIdentityManagementClient()
}
