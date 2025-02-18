#!/bin/sh

## run via '. setenv.sh'
##
##

if [ -z "${JAVA_HOME}" ]
then
  echo "JAVA_HOME not set"
  exit 0
fi

if [ ! -x "${JAVA_HOME}/bin/java" ]
then
  echo Cannot find java executable, check JAVA_HOME
  exit 0
fi

LIB=../../../lib

CLASSPATH=.
CLASSPATH=$CLASSPATH:../terminalsvc-sender/target/example-terminalsvc-sender-1.0.jar
CLASSPATH=$CLASSPATH:../terminalsvc-common/target/example-terminalsvc-common-1.0.jar
CLASSPATH=$CLASSPATH:../terminalsvc-receiver/target/example-terminalsvc-receiver-1.0.jar
CLASSPATH=$CLASSPATH:../../../esper-4.9.0.jar
CLASSPATH=$CLASSPATH:../lib/jboss-jms-api_1.1_spec-1.0.0.Final.jar
CLASSPATH=$CLASSPATH:../lib/jboss-client.jar
CLASSPATH=$CLASSPATH:$LIB/cglib-nodep-2.2.jar
CLASSPATH=$CLASSPATH:$LIB/commons-logging-1.1.1.jar
CLASSPATH=$CLASSPATH:$LIB/log4j-1.2.16.jar
CLASSPATH=$CLASSPATH:$LIB/antlr-runtime-3.2.jar

export CLASSPATH="$CLASSPATH"
