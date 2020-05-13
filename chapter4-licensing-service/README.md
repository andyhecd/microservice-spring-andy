# Chapter 4: Building microservices with Spring Cloud on Service Discovery
Chapter 4 builds on the material from Chapter 3 and introduces the concept of service registration and discovery patterns using Spring Cloud and Netflix's Eureka server. Using service discovery, you will be able to add and remove service instances without the clients having to know the physical locations of the service.

By the time you are done reading this chapter you will have built and/or deployed:

1. A Spring Cloud Config server that is deployed as Docker container and can manage a services configuration information using a file system or GitHub-based repository.
2. A Eureka server running as a Spring-Cloud based service. This service will allow multiple service instances to register with it. Clients that need to call a service will use Eureka to lookup the physical location of the target service.
3. A organization service that will manage organization data used within EagleEye.
4. A licensing service that will manage licensing data used within EagleEye.
5. A Postgres SQL database used to hold the data for these two services.

## Chapter 4 Summary
- The service discovery pattern is used to abstract away the physical location ofservices.
- A service discovery engine such as Eureka can seamlessly add and remove service instances from an environment without the service clients being impacted.
- Client-side load balancing can provide an extra level of performance and resiliency by caching the physical location of a service on the client making the service call.
- Eureka is a Netflix project that when used with Spring Cloud, is easy to set up and configure.
- You used three different mechanisms in Spring Cloud, Netflix Eureka, and Netflix Ribbon to invoke a service. These mechanisms included   
+ Using a Spring Cloud service DiscoveryClient
+ Using Spring Cloud and Ribbon-backed RestTemplate
+ Using Spring Cloud and Netflix’s Feign client

## Running the services for Chapter 4
`Note: the config server will continue to use project chapter3-confsvr`
#### Step 1: At the root folder of this project, run command *mvn clean package*
- You will get Spring Boot project build and packaged as configuration - jar;
- You will be able to see three new docker images generated with prefix *andyhecd/chapter4-*, including one discovery server and two clients
#### Step 2: continue to run command *docker run --name docker-mysql -dp 10700:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=microservice mysql* 
- You will be able to use command *docker exec -it docker-mysql bash* and then *mysql -u root -p root* to login mysql command line;
- Continue to run mysql command checking if database **microservice** exists.
#### Step 3: continue to run command *docker run -dp 10703:10703 andyhecd/chapter3-confsvr:0.0.1-SNAPSHOT*
- Try to access http://localhost:10703/licensingservice/prod, and prodction configuration responsed as expected 
#### Step 4: continue to run command *docker run -dp 10704:10704 andyhecd/chapter4-eureka-server:0.0.1-SNAPSHOT* 
- Try to access *http://localhost:10704/*, and displaying Eureka server status information as expected
#### Step 5: continue to run command *docker run -dp 11704:11704 andyhecd/chapter4-organization-service:0.0.1-SNAPSHOT* 
Open browser and try to access service *http://localhost:11704/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/*
- You should be able to see the correct response with json 
```
	{
		id: "e254f8c-c442-4ebe-a82a-e2fc1d1ff78a",
		name: "customer-crm-co",
		contactName: "Mark Balster",
		contactEmail: "mark.balster@custcrmco.com",
		contactPhone: "823-555-1212"
	}
```
#### Step 6: continue to run command *docker run -dp 12704:12704 andyhecd/chapter4-licensing-service:0.0.1-SNAPSHOT* 
Open browser and try to access service *http://localhost:12704/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/*
- You should be able to see the correct response with json 
```
	[
		{
			licenseId: "f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a",
			organizationId: "e254f8c-c442-4ebe-a82a-e2fc1d1ff78a",
			organizationName: "customer-crm-co",
			contactName: "Mark Balster",
			contactPhone: "823-555-1212",
			contactEmail: "mark.balster@custcrmco.com",
			productName: "CustomerPro",
			licenseType: "user",
			licenseMax: 100,
			licenseAllocated: 5,
			comment: "I AM A PROD PROPERTY OVERRIDE - changed after service started"
		},
		{
			licenseId: "t9876f8c-c338-4abc-zf6a-ttt1",
			organizationId: "e254f8c-c442-4ebe-a82a-e2fc1d1ff78a",
			organizationName: "customer-crm-co",
			contactName: "Mark Balster",
			contactPhone: "823-555-1212",
			contactEmail: "mark.balster@custcrmco.com",
			productName: "suitability-plus",
			licenseType: "user",
			licenseMax: 200,
			licenseAllocated: 189,
			comment: "I AM A PROD PROPERTY OVERRIDE - changed after service started"
		}
	]
```
## Recap: Spring Cloud Supported clients of Service Discovery 
#### Service discovery in action using Spring and Netflix Eureka
##### Step 1: Server side, building your Spring Eureka Server
- Service Discovery Server dependency:
```
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	</dependency>
```
- Service Discovery Server configuration, application.yml
```
server:
  port: 10704

spring:
  application:
    name: eureka-server

eureka:
  instance:
    hostname: host.docker.internal
  client:
    registerWithEureka: false #tells the service not to register with a Eureka service because this is the Eureka service
    fetchRegistry: false #Don’t cache registry information locally
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 5 #It will wait five minutes by default to give all of the services a chance to register with it before advertising them
```
- Enable Eureka server on Spring Boot Application with annotation @nableEurekaServer
##### Step 2: Client side, registering services with Spring Eureka
- Service Discovery Client dependency:
```
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
```
- Service Discovery Client configuration, application.yml:
```
spring:
  application:
    name: organizationservice #Logical name/application ID of the service that will be registered with Eureka, will represent a group service instance(normally, this property should be configured in bootstrap.yml)
server:
  port: 11704
eureka:
  instance:
    preferIpAddress: true #Register the IP of the service rather than the server name
  client:
    registerWithEureka: true #Register the service with Eureka
    fetchRegistry: true #Pull down a local copy of the registry
    serviceUrl:
        defaultZone: http://host.docker.internal:10704/eureka/ #Location of the Eureka Service
```
##### Step 3: Client side, using different client of service discovery to look up a service
###### Option 1: Spring Discovery client
- Enable discovery client on spring boot application with annotation @EnableDiscoveryClient, which is the trigger for Spring Cloud to enable the application to use the DiscoveryClient and Ribbon libraries.
- Then in your code, you will be able to autowire instance of DiscoveryClient(org.springframework.cloud.client.discovery.DiscoveryClient), which has ablity to get service instance by service logic name, a.k.a. application id.
- The ServiceInstance class is used to hold information about a specific instance of a service including its hostname, port and URI
- Finally, use a standerd spring REST template class to call the service via uri  
You will be able to use `http://localhost:12704/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/t9876f8c-c338-4abc-zf6a-ttt1/discovery/` to test this implementation.
###### Option 2: Spring Discovery client enabled RestTemplate
- Carry out Load Balanced RestTemplate Bean within Spring Boot Application,
```java
	@LoadBalanced //It tells Spring Cloud to create a Ribbon backed RestTemplate class
	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
```
- Use Eureka service ID of the service you want to call to build the target URL for Ribbon-backed RestTemplate instance which is autowired in you service class
```java
	@Component
	public class OrganizationRestTemplateClient {
		@Autowired
		RestTemplate restTemplate;

		public Organization getOrganization(String organizationId){
			ResponseEntity<Organization> restExchange =
					restTemplate.exchange(
							"http://organizationservice/v1/organizations/{organizationId}",
							HttpMethod.GET,
							null, Organization.class, organizationId);

			return restExchange.getBody();
		}
	}
```
You will be able to use `http://localhost:12704/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/t9876f8c-c338-4abc-zf6a-ttt1/rest/` to test this implementation.
###### Option 3: Netflix Feign client `Andy Awared`
An alternative to the Spring Ribbon-enabled RestTemplate class is Netflix’s Feign client library. The Feign library takes a different approach to calling a REST service by having the developer first define a Java interface and then annotating that interface with Spring Cloud annotations to map what Eureka-based service Ribbon will invoke. The Spring Cloud framework will dynamically generate a proxy class that will be used to invoke the targeted REST service. There’s no code being written for calling the service other than an interface definition.
- Enable Feign client on spring boot application with annotation @EnableFeignClients. 
- Define Feign client interface. How you define the getOrganization() method looks exactly like how you would expose an endpoint in a Spring Controller class
```java
	@FeignClient("organizationservice")
	public interface OrganizationFeignClient {
		@RequestMapping(
				method= RequestMethod.GET,
				value="/v1/organizations/{organizationId}",
				consumes="application/json")
		Organization getOrganization(@PathVariable("organizationId") String organizationId);
	}
```
- Autowire the feign client you defined and use it
```java
	organization = organizationFeignClient.getOrganization(organizationId);
```


 