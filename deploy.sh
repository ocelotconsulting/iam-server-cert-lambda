#!/usr/bin/env bash

set -x

SBT_HOME=/tmp/sbt

if ! [ -d $SBT_HOME ]; then
  mkdir -p $SBT_HOME/bin
  curl -L https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt-launch/0.13.12/sbt-launch.jar -o $SBT_HOME/bin/sbt-launch.jar

  cat << "EOF" > $SBT_HOME/bin/sbt

SBT_OPTS="-Xms512M -Xmx1536M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256M"
java $SBT_OPTS -jar `dirname $0`/sbt-launch.jar "$@"

EOF

fi

chmod u+x $SBT_HOME/bin/sbt
export PATH=$SBT_HOME/bin:$PATH

timestamp() {
  date +"%b-%d-%Y-%H_%M_%S"
}

dist_file=dist-`timestamp`.zip
echo $app_conf > ./src/main/resources/application.conf
sbt clean assembly
echo $cf_template > cf_template.json
echo $cf_parameters | sed "s|dist.zip|${LAMBCI_REPO}/${dist_file}|g" > cf_parameters.json
npm install aws-sdk-cli
AWS_CLI=./node_modules/aws-sdk-cli/lib/index.js
node $AWS_CLI cp ./target/scala-2.11/scala-letsencrypt-iam-lambda-assembly-1.0.jar s3://lambci-build-artifacts/$LAMBCI_REPO/$dist_file
node $AWS_CLI update-stack -t cf_template.json -p cf_parameters.json letsencrypt-iam-stack
