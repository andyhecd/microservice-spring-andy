# Chapter 3: Building microservices with Spring Cloud Config
Chapter 3 introduces the Spring Cloud Config service and how you can use it managed the configuration of your microservices. By the time you are done reading this chapter you will have built and/or deployed:
1. A Spring Cloud Config server that is deployed as Docker container and can manage a services configuration information using a file system or GitHub-based repository.
2. A organization service that will manage organization data used within EagleEye.
3. A licensing service that will manage licensing data used within EagleEye.
4. A Postgres SQL database used to hold the data for these two services.
## Chapter 3 Summary
+ Spring Cloud configuration server allows you to set up application properties with environment specific values.
+ Spring uses Spring profiles to launch a service to determine what environment properties are to be retrieved from the Spring Cloud Config service.
+ Spring Cloud configuration service can use a file-based or Git-based application configuration repository to store application properties.
+ Spring Cloud configuration service allows you to encrypt sensitive property files using symmetric and asymmetric encryption.
## Running the services for Chapter 3
#### Step 1: At the root folder of this project, run command *mvn clean package*
- You will get Spring Boot project build and packaged as configuration - jar;
- You will be able to see a new docker image generated with name andyhecd/chapter3-confsvr:0.0.1-SNAPSHOT
- You will be able to see a new docker image generated with name andyhecd/chapter3-licensing-service:0.0.1-SNAPSHOT
#### Step 2: continue to run command *docker run --name docker-mysql -dp 10700:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=microservice mysql* 
- You will be able to use command *docker exec -it docker-mysql bash* and then *mysql -u root -p root* to login mysql command line;
- Continue to run mysql command checking if database **microservice** exists.
#### Step 3: continue to run command *docker run -dp 10703:10703 andyhecd/chapter3-confsvr:0.0.1-SNAPSHOT* 
- Try to access *http://localhost:10703/licensingservice/prod*, and prodction configuration responsed as expected
#### Step 4: continue to run command *docker run -dp 11703:11703 andyhecd/chapter3-licensing-service:0.0.1-SNAPSHOT* 
#### Step 5: Open browser and try to access service *http://localhost:11703/v1/organizations/442adb6e-fa58-47f3-9ca2-ed1fecdfe86c/licenses/38777179-7094-4200-9d61-edb101c6ea84*
- You should be able to see the correct response with json 
```json
	{
		licenseId: "38777179-7094-4200-9d61-edb101c6ea84",
		organizationId: "442adb6e-fa58-47f3-9ca2-ed1fecdfe86c",
		productName: "HR-PowerSuite",
		licenseType: "user",
		licenseMax: 100,
		licenseAllocated: 4,
		comment: "I AM A PROD PROPERTY OVERRIDE"
	}
```
#### Step 6: Refreshing your properties using Spring Cloud configuration server
- After configuration file changed, use postman or other tool to send http post request to *http://localhost:11703/actuator/refresh*
- Make sure spring boot started with annotation @RefreshScope
- Make sure spring actuator feature enabled. In application.yml file:
```yml
	management:
	  endpoint:
	    shutdown:
	      enabled: false
	  endpoints:
	    web:
	      exposure:
		include: "*"
```
- Once done, the custom properties values will be up to date on host *localhost:11703*.
#### Note: only custom configuration will be reread, which means the properties you added.
```
You will get response once posting successfully
[
    "config.client.version",
    "example.property"
]
```
#### Note: the value will be up to date only working with @ConfigurationProperties rather than @Component + @Value.
#### Step 7: Protecting sensitive configuration inforamtion
- Make sure your configuration server running jvm with jce. Here is sample command line for installing jce with docker file.
```bash
# Add Java Cryptography Extension - JCE -------------------
RUN curl -q -L -C - -b "oraclelicense=accept-securebackup-cookie" -o /tmp/jce_policy-8.zip -O http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip \
    && unzip -oj -d /usr/lib/jvm/java-1.8-openjdk/jre/lib/security /tmp/jce_policy-8.zip \*/\*.jar \
    && rm /tmp/jce_policy-8.zip
```
- Disable the server-side decryption of properties in Spring Cloud Config with configuration:
```
spring.cloud.config.server.encrypt.enabled: false
```
- Make sure both server and clint use the same enviromenrt variable ENCRYPT_KEY
- Make clint project uses spring-security-rsa JARs
- HTTP Post the content you wanna encrypt to configuration server *http://localhost:10703/encrypt*
- Put the encrypted content with leading constant string **"{cipher}"** to the configuration file
- Try to access configuration file via *http://localhost:10703/licensingservice/prod*, you will see spring.datasource.password is encrypted.
 
