#!/bin/sh
echo "********************************************************"
echo "Starting Organization Service "
echo "********************************************************"
java -Dspring.profiles.active=$PROFILE -jar $JAR_FILE_PATH