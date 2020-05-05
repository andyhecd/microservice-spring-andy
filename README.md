# microservice-spring-andy
All the practices are relevant to book Manning Spring Micro-services in Action 2017.6
### Chapter 1: Welcome to the cloud, Spring
##### Step 1: At the root folder of this project, run command *mvn clean package*
- You will get Spring Boot project build and packaged as configuration - jar;
- You will be able to see a new docker image generated with name andyhecd/chapter1:0.0.1-SNAPSHOT
##### Step 2: continue to run command *docker run -p 10701:10701 andyhecd/chapter1:0.0.1-SNAPSHOT* 
##### Step 3: Open browser and try to access service *http://localhost:10701/hello/andy/hee*
- You should be able to see the correct response with json 
```
	{
		message: "Hello andy hee"
	}
```
### Chapter 2: Building microservices with Spring Boot
##### Step 1: At the root folder of this project, run command *mvn clean package*
- You will get Spring Boot project build and packaged as configuration - jar;
- You will be able to see a new docker image generated with name andyhecd/chapter2-licensing-service:0.0.1-SNAPSHOT
##### Step 2: continue to run command *docker run -p 10702:10702 andyhecd/chapter2-licensing-service:0.0.1-SNAPSHOT* 
##### Step 3: Open browser and try to access service *http://localhost:10702/v1/organizations/sapibsocd/licenses/csccn_andy*
- You should be able to see the correct response with json 
```
	{
		id: "csccn_andy",
		organizationId: "sapibsocd",
		productName: "Test Product Name",
		licenseType: "PerSeat"
	}
```
##### Step 4: continue to access service *http://localhost:10702/actuator*
- You will get system status information provided by **spring-boot-starter-actuator**
```
{
	_links: {
		self: {
			href: "http://localhost:10702/actuator",
			templated: false
		},
		health: {
			href: "http://localhost:10702/actuator/health",
			templated: false
		},
		health-path: {
			href: "http://localhost:10702/actuator/health/{*path}",
			templated: true
		},
		info: {
			href: "http://localhost:10702/actuator/info",
			templated: false
		}
	}
}
```
### Chapter 3: Building microservices with Spring Cloud Config
##### Step 1: At the root folder of this project, run command *mvn clean package*
- You will get Spring Boot project build and packaged as configuration - jar;
- You will be able to see a new docker image generated with name andyhecd/chapter3-confsvr:0.0.1-SNAPSHOT
- You will be able to see a new docker image generated with name andyhecd/chapter3-licensing-service:0.0.1-SNAPSHOT
##### Step 2: continue to run command *docker run --name microservice-mysql -p 10700:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql* 
##### Step 3: continue to run command *docker run -dp 10703:10703 andyhecd/chapter3-confsvr:0.0.1-SNAPSHOT* 
##### Step 4: continue to run command *docker run -dp 11703:11703 andyhecd/chapter3-licensing-service:0.0.1-SNAPSHOT* 
##### Step 5: Open browser and try to access service *http://localhost:10702/v1/organizations/sapibsocd/licenses/csccn_andy*
- You should be able to see the correct response with json 
```
	{
		id: "csccn_andy",
		organizationId: "sapibsocd",
		productName: "Test Product Name",
		licenseType: "PerSeat"
	}
```
