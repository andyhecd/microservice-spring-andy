FROM openjdk:8-jdk-alpine
EXPOSE 10703

ARG JAR_FILE
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl

# Add Java Cryptography Extension - JCE -------------------
RUN curl -q -L -C - -b "oraclelicense=accept-securebackup-cookie" -o /tmp/jce_policy-8.zip -O http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip \
    && unzip -oj -d /usr/lib/jvm/java-1.8-openjdk/jre/lib/security /tmp/jce_policy-8.zip \*/\*.jar \
    && rm /tmp/jce_policy-8.zip
    
ENV ENCRYPT_KEY IMSYMMETRIC

RUN mkdir -p /usr/local/chapter3
ADD target/${JAR_FILE} /usr/local/chapter3/
ADD run.sh run.sh
RUN chmod +x run.sh

ENV JAR_FILE_PATH /usr/local/chapter3/${JAR_FILE}
CMD ./run.sh