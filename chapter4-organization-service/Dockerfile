FROM openjdk:8-jdk-alpine
EXPOSE 11704
ARG JAR_FILE

RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter4
ADD target/${JAR_FILE} /usr/local/chapter4/

# Add docker-compose-wait tool -------------------
ENV WAIT_VERSION 2.7.3
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

ADD run.sh run.sh
RUN chmod +x run.sh

ENV JAR_FILE_PATH /usr/local/chapter4/${JAR_FILE}
ENV ENCRYPT_KEY IMSYMMETRIC
CMD ./run.sh