# iam-server-cert-lambda
An AWS Lambda function to take a received SNS message based upon an S3 event from 
[node-letsencrypt-lambda](https://github.com/ocelotconsulting/node-letsencrypt-lambda), and update a related certificate
in AWS IAM. This project will serve as a follow-on project to 
[node-letsencrypt-lambda](https://github.com/ocelotconsulting/node-letsencrypt-lambda), helping to
further the automation of configuring SSL certificates in AWS.

## AWS Configuration
This project requires a little [configuration](AWS.md) to be used in AWS.

## Execution
1. Git-clone this repository.

        $ git clone git@github.com:ocelotconsulting/iam-server-cert-lambda.git

2. Modify configuration (TBD).

3. Create S3 buckets, IAM role, then test locally:

        $ sbt clean assembly

4. Upload JAR for JVM Lambda to AWS. In handler configuration, use the string for the public handler function
`com.ocelotconsulting.ssl.IAMServerCertificateLambda::configureIAMCert`

## Disclaimer
So far, AWS IAM only allows certificates to be uploaded, renamed, and deleted,
which means that when a certificate expires, we must delete and re-upload it, which could
break certain integrations relying on a specific certificate.
