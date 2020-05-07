FROM openjdk:8-jdk-alpine
EXPOSE 10701

ARG JAR_FILE
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter1
ADD target/${JAR_FILE} /usr/local/chapter1/
ADD run.sh run.sh
RUN chmod +x run.sh

ENV JAR_FILE_PATH /usr/local/chapter1/${JAR_FILE}
CMD ./run.sh