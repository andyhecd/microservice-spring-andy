# Chapter 3: Building microservices with Spring Cloud Config
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
```
	{
		licenseId: "38777179-7094-4200-9d61-edb101c6ea84",
		organizationId: "442adb6e-fa58-47f3-9ca2-ed1fecdfe86c",git 
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
```
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
