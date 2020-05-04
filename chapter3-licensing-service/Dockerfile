FROM openjdk:8-jdk-alpine
EXPOSE 11703
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter3
ADD target/chapter3-licensing-service.jar /usr/local/chapter3/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh