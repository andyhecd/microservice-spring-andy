FROM openjdk:8-jdk-alpine
EXPOSE 10002
ARG JAR_FILE

RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/microservice
ADD target/${JAR_FILE} /usr/local/microservice/

ADD run.sh run.sh
RUN chmod +x run.sh

ENV JAR_FILE_PATH /usr/local/microservice/${JAR_FILE}
CMD ./run.sh