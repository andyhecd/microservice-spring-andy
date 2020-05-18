# Chapter 5: When bad things happen: client resiliency patterns with Spring Cloud and Netflix
Client resiliency software patterns are focused on protecting a remote resource’s (another microservice call or database lookup) client from crashing when the remote resource is failing because that remote service is throwing errors or performing poorly. The goal of these patterns is to allow the client to “fail fast,” not consume valuable resources such as database connections and thread pools, and prevent the problem of the remote service from spreading “upstream” to consumers of the client.
There are four client resiliency patterns:
1. Client-side load balancing, which removes that service instance from the pool of available service locations and prevents any future service calls from hitting that service instance.
2. Circuit breakers, which will pop, failing fast and preventing future calls to the failing remote resource.
3. Fallbacks, which leads service consumer to execut an alternative code path and try to carry out an action through another means.
4. Bulkheads, which breaks the calls to remote resources into their own thread pools and reduce the risk that a problem with one slow remote resouce call will take down the entir application.

## Chapter 5 Summary
- When designing highly distributed applications such as a microservice-based application, client resiliency must be taken into account.
- Outright failures of a service (for example, the server crashes) are easy to detect and deal with.
- A single poorly performing service can trigger a cascading effect of resource exhaustion as threads in the calling client are blocked waiting for a service to complete.
- Three core client resiliency patterns are the circuit-breaker pattern, the fallback pattern, and the bulkhead pattern.
- The circuit breaker pattern seeks to kill slow-running and degraded system calls so that the calls fail fast and prevent resource exhaustion.
- The fallback pattern allows you as the developer to define alternative code paths in the event that a remote service call fails or the circuit breaker for the call fails.
- The bulk head pattern segregates remote resource calls away from each other, isolating calls to a remote service into their own thread pool. If one set of service calls is failing, its failures shouldn’t be allowed to eat up all the resources in the application container.
- Spring Cloud and the Netflix Hystrix libraries provide implementations for the circuit breaker, fallback, and bulkhead patterns.
- The Hystrix libraries are highly configurable and can be set at global, class, and thread pool levels.
- Hystrix supports two isolation models: THREAD and SEMAPHORE.
- Hystrix’s default isolation model, THREAD, completely isolates a Hystrix protected call, but doesn’t propagate the parent thread’s context to the Hystrix managed thread.
- Hystrix’s other isolation model, SEMAPHORE, doesn’t use a separate thread to make a Hystrix call. While this is more efficient, it also exposes the service to unpredictable behavior if Hystrix interrupts the call.
- Hystrix does allow you to inject the parent thread context into a Hystrix managed Thread through a custom HystrixConcurrencyStrategy implementation.

## Implementment using Hystrix
1. Add maven dependency
```xml
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
	</dependency>
```
2. Add @EnableCircuitBreaker on the spring bootstrap application
3. Add @HystrixCommand on the function
   - circuit breaker
   - fallback
   - bulkhead
```java
	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", threadPoolKey = "licenseByOrgThreadPool", threadPoolProperties = {
			// define the maximum number of threads in the thread pool, default to 10
			@HystrixProperty(name = "coreSize", value = "30"),
			// define a queue that sits in front of your thread
			// pool and that can queue incoming requests, default to -1(no queue is used and
			// instead Hystrix will block until a thread becomes available for processing.)
			@HystrixProperty(name = "maxQueueSize", value = "10") }, commandProperties = {
					// set the length of the timeout (in milliseconds) of the circuit breaker,
					// default is 1000
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
					// amount of consecutive calls that must occur within a 10-second window before
					// Hystrix will consider to trip the circuit breaker for the call. Default to 20
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
					// the percentage of calls that must fail(due to timeouts, an exception being
					// thrown, or a HTTP 500 being returned) after the
					// circuitBreaker.requestVolumeThreshold value has been passed before the
					// circuit breaker it tripped, Default to 50
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
					// the amount of time Hystrix will sleep once the circuit breaker is tripped
					// before Hystrix will allow another call through to see if the service is
					// healthy again. Default to 5000
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
					// control the size of the window that will be used by Hystrix to monitor for
					// problems with a service call, default to 10000, a.k.a 10-second window,
					// Default to 10000
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
					// control the number of times statistics are collected in the window you’ve
					// defined, Default to 10
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5") })
	public List<License> getLicensesByOrgWithSleep(String organizationId, int sleppInMilliseconds) {
		sleep(sleppInMilliseconds);
		return getLicensesByOrg(organizationId);
	}
```

## ThreadLocal and Hystrix: HystrixConcurrencyStrategy
1. Define your custom Hystrix Concurrency Strategy class. 
2. Define a Java *Callable* class to inject the UserContext(your bean to hold user context information) into the Hystrix Command
3. Configure Spring Cloud to use your custom Hystrix Concurrency Strategy
Access *http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/sleep/10* with http request get header *tmx-correlation-id=andy-he-cd*
- With implementation of package license.util, only ThreadLocal supported, the values can only share with processes in the parent thread. 
```console
2020-05-18 16:43:14.480 DEBUG 20036 --- [io-10003-exec-5] c.s.a.m.licenses.util.UserContextFilter  : UserContextFilter Correlation id: andy-he-cd
2020-05-18 16:43:14.487 DEBUG 20036 --- [io-10003-exec-5] c.s.a.m.l.c.LicenseServiceController     : LicenseServiceController Correlation id: andy-he-cd
2020-05-18 16:43:14.513 DEBUG 20036 --- [OrgThreadPool-4] c.s.a.m.licenses.service.LicenseService  : LicenseService.getLicensesByOrg  Correlation id: 
```
- Once plugin the HystrixConcurrencyStrategy, the information can be pass to Hystrix managed thread.
```console
2020-05-18 17:11:29.403 DEBUG 15392 --- [io-10003-exec-2] c.s.a.m.licenses.util.UserContextFilter  : UserContextFilter Correlation id: andy-he-cd
2020-05-18 17:11:29.515 DEBUG 15392 --- [io-10003-exec-2] c.s.a.m.l.c.LicenseServiceController     : LicenseServiceController Correlation id: andy-he-cd
2020-05-18 17:11:30.350 DEBUG 15392 --- [OrgThreadPool-1] c.s.a.m.licenses.service.LicenseService  : LicenseService.getLicensesByOrg  Correlation id: andy-he-cd
```

## Verify the services for Chapter 5
#### Access *http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/sleep/100*, get response soon
```json
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
		comment: "I AM IN THE DEV-changed after service started - 2"
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
		comment: "I AM IN THE DEV-changed after service started - 2"
	}
]
```
#### Access *http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/sleep/100000*, get response soon also with failed. Because timeout setting is 2 seconds.
```json
[
	{
		licenseId: "0000000-00-00000",
		organizationId: "e254f8c-c442-4ebe-a82a-e2fc1d1ff78a",
		organizationName: "",
		contactName: "",
		contactPhone: "",
		contactEmail: "",
		productName: "Sorry no licensing information currently available",
		licenseType: null,
		licenseMax: null,
		licenseAllocated: null,
		comment: null
	}
]
```

 
