# Chapter 6: Service Routing with Sping Cloud
A service gateway acts as an intermedary between the service client and a service being invoked. With a service gateway in place, your service clients never directly call the URL of an individual service, but instead place all calls to the service gateway. The service gateway pulls apart the path coming in from the service client call and determines what service the service client is trying to invoke.
Examples of cross-cutting conerns that can be implemented in a servicec gateway include:
- Static Routing. A service gateway places all service calls behind a single URL and API route. This simplifies development as developers only have to know about one service endpoint for all of their services.
- Dynamic Routing. A service gateway can inspect incoming service requests and, based on data from the incoming request, perform intelligent routing based on who the service caller is.
- Authentication and Authorization. Because all service calls route through a service gateway, the service gateway is a natural place to check whether the caller of a service has authenticated themselves and is authorized to make the service call.
- Metric Collection and Logging. A service gateway can be used to collect metrics and log information as a service call passes through the service gateway. This doesn't mean that shouldn't you still collect metrics from within your individual services, but rather a services gateway allows you to centralize collection of many of your basic metrics, like the number of times the service is invoked and service response time.

Notes:
- Load balancers are still useful when out in front of individual groups of services. But it becomes a bottleneck if having a load balancer sit in front of all your service instances.
- Keep any code you write for your service gateway stateless. Don't store any information in memory for the service gateway.
- Keep the code you write for your service gateway light. Complex code with multiple database calls can be the source of difficult-to-track-down performance problems in the service gateway.

## Chapter 6 Summary

## Implementation based on Spring Cloud Gateway
Manning Spring Microservice in Action version 2017.6 impelemnts service routing by Zuul. On 2020, Zuul is out of date, I use spring cloud gateway instead.
1. Add maven dependency
```xml
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-zuul</artifactId>
	</dependency>
```
2. Add @EnableZuulProxy on the spring bootstrap application. @EnableZuulServer is used when you want to build your own routing service and not use any Zuul prebuilt capabilities.

## Configure Zuul to communicate with Eureka
The Zuul proxy server is designed by default to work on the Spring products. As such, Zuul will automatically use Eureka to look up services by their service IDs and then use Netflix Ribbon to do client-side load balancing of requests from within Zuul.
```yml
eureka:
  instance:
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
        defaultZone: http://host.docker.internal:10002/eureka/
```

## Configure routes in Zuul
- Automated mapping routes via service discovery
If you don't specify any routes, Zuul will automatically use the Eureka service ID of the service being called and map it to a downstream service instance. Try:
```console
http://localhost:10005/organizationservice/v1/organizations/e254f8c-c442-4ebea82a-e2fc1d1ff78a
```
Use *http://localhost:10005/routes* to see the routes being managed by the Zuul server.
- Mapping routes manually using service discovery
Add configuration to application.yml of Zuul server
```yml
zull:
  ignore-services: * #disable auto-mapped configuration. * means ignore all. You can use comma-separated list of Eureka service-IDs.
  prefix: /api # All defined services will be prefixed with /api
  routes:
    organizationservice: /organization/** # your organizationservice mapped to the organization endpoint respectively
	licensingservice: /licensing/**
```
Try:
```console
http://localhost:10005/api/organization/v1/organizations/e254f8c-c442-4ebea82a-e2fc1d1ff78a
```
- Manual mapping of routes using static URLs
Zuul can be used to route services that aren’t managed by Eureka. 

## Reference
> cloud.spring.io: [Router and Filter: Zuul](https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html)


 
