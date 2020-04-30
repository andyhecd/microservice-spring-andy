FROM openjdk:8-jdk-alpine
EXPOSE 10702
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter2
ADD target/chapter2-licensing-service.jar /usr/local/chapter2/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh