# letsencrypt-iam-lambda
An AWS Lambda function to take a received S3 event, and update a related certificate in AWS IAM.

## Status
Under development, but this project will serve as a follow-on project to
[node-letsencrypt-lambda](https://github.com/ocelotconsulting/node-letsencrypt-lambda), helping to further the
automation of configuring SSL certificates in AWS.

## Execution
1. Git-clone this repository.

        $ git clone git@github.com:ocelotconsulting/letsencrypt-iam-lambda.git

2. Modify configuration (TBD).

3. Create S3 buckets, IAM role, then test locally:

        $ sbt clean assembly

4. Upload JAR for JVM Lambda to AWS.
