package com.ocelotconsulting.ssl

/**
  * Created by Larry Anderson on 10/8/16.
  */
case class CertificateFile(key: Key, cert: String, issuerCert: String)
case class Key(privateKeyPem: String, publicKeyPem: String, privateKeyJwk: PrivateKeyJWK, publicKeyJWK: PublicKeyJWK)
case class PrivateKeyJWK(kty: String, n: String, e: String, d: String, p: String, q: String, dp: String, dq: String, qi: String)
case class PublicKeyJWK(kty: String, n: String, e: String)