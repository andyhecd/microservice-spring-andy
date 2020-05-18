#!/bin/sh
echo "********************************************************"
echo "Starting Licensing Service "
echo "********************************************************"
java -Dspring.profiles.active=$PROFILE -jar $JAR_FILE_PATH