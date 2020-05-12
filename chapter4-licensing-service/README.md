# Chapter 4: Building microservices with Spring Cloud on Service Discovery
Chapter 4 builds on the material from Chapter 3 and introduces the concept of service registration and discovery patterns using Spring Cloud and Netflix's Eureka server. Using service discovery, you will be able to add and remove service instances without the clients having to know the physical locations of the service.

By the time you are done reading this chapter you will have built and/or deployed:

1. A Spring Cloud Config server that is deployed as Docker container and can manage a services configuration information using a file system or GitHub-based repository.
2. A Eureka server running as a Spring-Cloud based service. This service will allow multiple service instances to register with it. Clients that need to call a service will use Eureka to lookup the physical location of the target service.
3. A organization service that will manage organization data used within EagleEye.
4. A licensing service that will manage licensing data used within EagleEye.
5. A Postgres SQL database used to hold the data for these two services.

Note: the config server will continue to use project chapter3-confsvr
## Running the services for Chapter 4
#### Step 1: At the root folder of this project, run command *mvn clean package*
- You will get Spring Boot project build and packaged as configuration - jar;
- You will be able to see three new docker images generated with prefix *andyhecd/chapter4-*, including one discovery server and two clients
#### Step 2: continue to run command *docker run --name docker-mysql -dp 10700:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=microservice mysql* 
- You will be able to use command *docker exec -it docker-mysql bash* and then *mysql -u root -p root* to login mysql command line;
- Continue to run mysql command checking if database **microservice** exists.
#### Step 3: continue to run command docker run -dp 10703:10703 andyhecd/chapter3-confsvr:0.0.1-SNAPSHOT
- Try to access http://localhost:10703/licensingservice/prod, and prodction configuration responsed as expected 
#### Step 4: continue to run command *docker run -dp 10704:10704 andyhecd/chapter4-eureka-server:0.0.1-SNAPSHOT* 
- Try to access *http://localhost:10704/*, and displaying Eureka server status information as expected
#### Step 5: continue to run command *docker run -dp 11704:11704 andyhecd/chapter4-organization-service:0.0.1-SNAPSHOT* 
Open browser and try to access service *http://localhost:12704/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/*
- You should be able to see the correct response with json 
```
	
```
#### Step 6: continue to run command *docker run -dp 12704:12704 andyhecd/chapter4-licensing-service:0.0.1-SNAPSHOT* 
Open browser and try to access service *http://localhost:12704/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/*
- You should be able to see the correct response with json 
```
	
```
## Recap: Spring Cloud Supported ways of Service Discovery 
#### 
 