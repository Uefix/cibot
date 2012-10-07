#!/bin/bash
cd ..
mvn clean install
cp cibot-controller/target/cibot-controller.jar ../../executable