#!/bin/bash
#export LD_LIBRARY_PATH=/Users/uefix/cibot/executable

CIBOT_PATH=`pwd`
CIBOT_CLASSPATH="$CIBOT_PATH/cibot-controller-1.0.jar:$CIBOT_PATH"

echo CIBOT_CLASSPATH=$CIBOT_CLASSPATH

java -d32 -cp $CIBOT_CLASSPATH com.cibot.ControllerMain