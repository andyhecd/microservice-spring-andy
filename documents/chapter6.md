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

## Implementation based on Spring Cloud Gateway
Manning Spring Microservice in Action version 2017.6 impelemnts service routing by Zuul. On 2020, Zuul is out of date, I use spring cloud gateway instead.
- Add maven dependency(add eureka client, actuator and Spring config as well)
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```
- Enable Eureka client
```yml
eureka:
  instance:
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://${com.sap.andyhecd.microservice.eureka.server.host}:${com.sap.andyhecd.microservice.eureka.server.port}/eureka/
    healthcheck:
      enabled: true
```
- Enable acturator endpoint
```yml
management:
  endpoint:
    gateway:
      enabled: true
  endpoints:
    web:
      exposure:
        include: gateway
```
- Enable gateway with Eureka
```yml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能
          lower-case-service-id: true #使用小写服务名，默认是大写
```
- Till now, you will see auto mapped Eureka-based routes via accessing *http://localhost:10005/actuator/gateway/routes*
```json
[
	{
		predicate: "Paths: [/gatewayserver/**], match trailing slash: true",
		metadata: {
			management.port: "10005"
		},
		route_id: "ReactiveCompositeDiscoveryClient_GATEWAYSERVER",
		filters: [
			"[[RewritePath /gatewayserver/(?<remaining>.*) = '/${remaining}'], order = 1]"
		],
		uri: "lb://GATEWAYSERVER",
		order: 0
	},
	{
		predicate: "Paths: [/licensingservice/**], match trailing slash: true",
		metadata: {
			management.port: "10003"
		},
		route_id: "ReactiveCompositeDiscoveryClient_LICENSINGSERVICE",
		filters: [
			"[[RewritePath /licensingservice/(?<remaining>.*) = '/${remaining}'], order = 1]"
		],
		uri: "lb://LICENSINGSERVICE",
		order: 0
	},
	{
		predicate: "Paths: [/organizationservice/**], match trailing slash: true",
		metadata: {
			management.port: "10004"
		},
		route_id: "ReactiveCompositeDiscoveryClient_ORGANIZATIONSERVICE",
		filters: [
			"[[RewritePath /organizationservice/(?<remaining>.*) = '/${remaining}'], order = 1]"
		],
		uri: "lb://ORGANIZATIONSERVICE",
		order: 0
	}
]
```
- Which means you will be able to access services from licensing service module via:
> http://localhost:10005/licensingservice/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/
>> The gateway host and Eureka server awared application registered.
- Addtionally, if add filter configuration like:
```yml
spring:
  cloud:
    gateway:
      routes:
          - id: prefixpath_licensing_route
            uri: lb://LICENSINGSERVICE #此处需要使用lb协议
            predicates:
              - Path=/license/**
            filters:
              - StripPrefix=1
              - PrefixPath=/v1
          - id: prefixpath_organization_route
            uri: lb://ORGANIZATIONSERVICE #此处需要使用lb协议
            predicates:
              - Path=/org/**
            filters:
              - StripPrefix=1
              - PrefixPath=/v1
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能
          lower-case-service-id: true #使用小写服务名，默认是大写
```
then, following service request are valid:
> http://localhost:10005/org/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a, which is the same as http://localhost:10005/organizationservice/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a;
> http://localhost:10005/license/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/ which is the same as http://localhost:10005/licensingservice/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/.
Note: 
1. the prefix **org** and **license** will be removed by configuration *- StripPrefix=1*; and determine to protocal *1b* and let Eureka server analyses the real service url, then add prefix */v1*. Finally, combine the remaning url pathes.
2. the access way 2 uses by default eureka-bases routes directly.


 
