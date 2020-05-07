FROM openjdk:8-jdk-alpine
EXPOSE 10702

ARG JAR_FILE
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter2
ADD target/${JAR_FILE} /usr/local/chapter2/
ADD run.sh run.sh
RUN chmod +x run.sh

ENV JAR_FILE_PATH /usr/local/chapter2/${JAR_FILE}
CMD ./run.sh