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
- You used three different mechanisms in Spring Cloud, Netflix Eureka, and Netflix Ribbon to invoke a service. These mechanisms included：
   + Using a Spring Cloud service DiscoveryClient
   + Using Spring Cloud and Ribbon-backed RestTemplate
   + Using Spring Cloud and Netflix’s Feign client

## Verify the services for Chapter 4
#### Access *http://localhost:10002/*, and displaying Eureka server status information as expected
#### Access service *http://localhost:10004/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/*
```json
	{
		id: "e254f8c-c442-4ebe-a82a-e2fc1d1ff78a",
		name: "customer-crm-co",
		contactName: "Mark Balster",
		contactEmail: "mark.balster@custcrmco.com",
		contactPhone: "823-555-1212"
	}
```
- If you change the port and start another organization service container, then:
   - use *docker run -dp 10014:10004 -e ENCRYPT_KEY=IMSYMMETRIC andyhecd/organization-service:0.0.1-SNAPSHOT*
   - access *http://localhost:10002/eureka/apps/organizationservice*, you will find out two service instances;
   - or access *http://localhost:10002/*, you will see two avaliable zones for `organizationservice` group.
#### Access service *http://localhost:10003/v1/organizations/442adb6e-fa58-47f3-9ca2-ed1fecdfe86c/licenses/*
```json
	[
		{
			licenseId: "08dbe05-606e-4dad-9d33-90ef10e334f9",
			organizationId: "442adb6e-fa58-47f3-9ca2-ed1fecdfe86c",
			organizationName: "HR-PowerSuite",
			contactName: "Doug Drewry",
			contactPhone: "920-555-1212",
			contactEmail: "doug.drewry@hr.com",
			productName: "WildCat Application Gateway",
			licenseType: "core-prod",
			licenseMax: 16,
			licenseAllocated: 16,
			comment: "I AM A PROD PROPERTY OVERRIDE - changed after service started"
		},
		{
			licenseId: "38777179-7094-4200-9d61-edb101c6ea84",
			organizationId: "442adb6e-fa58-47f3-9ca2-ed1fecdfe86c",
			organizationName: "HR-PowerSuite",
			contactName: "Doug Drewry",
			contactPhone: "920-555-1212",
			contactEmail: "doug.drewry@hr.com",
			productName: "HR-PowerSuite",
			licenseType: "user",
			licenseMax: 100,
			licenseAllocated: 4,
			comment: "I AM A PROD PROPERTY OVERRIDE - changed after service started"
		}
	]
```
#### If you stop eureka server, you can still get license with organization information successfully. Because the clint-side load balancer cached the pyhsical location of service instances.

## Recap: Spring Cloud Supported clients of Service Discovery 
#### Service discovery in action using Spring and Netflix Eureka
##### Step 1: Server side, building your Spring Eureka Server
- Service Discovery Server dependency:
```maven
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	</dependency>
```
- Service Discovery Server configuration, application.yml
```yml
server:
  port: 10002

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
```maven
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
```
- Service Discovery Client configuration, application.yml:
```yml
spring:
  application:
    name: organizationservice #Logical name/application ID of the service that will be registered with Eureka, will represent a group service instance(normally, this property should be configured in bootstrap.yml)
server:
  port: 10004
eureka:
  instance:
    preferIpAddress: true #Register the IP of the service rather than the server name
  client:
    registerWithEureka: true #Register the service with Eureka
    fetchRegistry: true #Pull down a local copy of the registry
    serviceUrl:
        defaultZone: http://host.docker.internal:10002/eureka/ #Location of the Eureka Service
```
##### Step 3: Client side, using different client of service discovery to look up a service
###### Option 1: Spring Discovery client
- Enable discovery client on spring boot application with annotation @EnableDiscoveryClient, which is the trigger for Spring Cloud to enable the application to use the DiscoveryClient and Ribbon libraries.
- Then in your code, you will be able to autowire instance of DiscoveryClient(org.springframework.cloud.client.discovery.DiscoveryClient), which has ablity to get service instance by service logic name, a.k.a. application id.
- The ServiceInstance class is used to hold information about a specific instance of a service including its hostname, port and URI
- Finally, use a standerd spring REST template class to call the service via uri  
You will be able to use `http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/t9876f8c-c338-4abc-zf6a-ttt1/discovery/` to test this implementation.
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
You will be able to use `http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/t9876f8c-c338-4abc-zf6a-ttt1/rest/` to test this implementation.
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
You will be able to use `http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/t9876f8c-c338-4abc-zf6a-ttt1/feign/` to test this implementation.

 
