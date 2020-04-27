FROM openjdk:8-jdk-alpine
EXPOSE 10701
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/chapter1
ADD target/chapter1.jar /usr/local/chapter1/
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh