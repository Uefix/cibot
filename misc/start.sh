#!/bin/bash
#export LD_LIBRARY_PATH=/Users/uefix/cibot/executable

CIBOT_PATH=`pwd`
CIBOT_CONFIGFILE="$CIBOT_PATH/cibot.xml"

echo CIBOT_CONFIGFILE=$CIBOT_CONFIGFILE

java -d32 -jar cibot-controller.jar $CIBOT_CONFIGFILE