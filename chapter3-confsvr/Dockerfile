FROM openjdk:8-jdk-alpine
EXPOSE 10703

ARG JAR_FILE
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter3
ADD target/${JAR_FILE} /usr/local/chapter3/
ADD run.sh run.sh
RUN chmod +x run.sh

ENV JAR_FILE_PATH /usr/local/chapter3/${JAR_FILE}
CMD ./run.sh