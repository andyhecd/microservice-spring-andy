#!/bin/sh
echo "********************************************************"
echo "Starting chapter3 licensing service "
echo "********************************************************"
java -Dspring.profiles.active=$PROFILE -jar $JAR_FILE_PATH