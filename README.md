# microservice-spring-andy
All the practices are relevant to book Manning Spring Micro-services in Action 2017.6
### Build and Run all images:
- Step 1: At the root folder of this project, run command *mvn clean package*
- Step 2: Continue to run *docker-compose up*
- Step 3: Access each of service below to verify every chapter.
***
> Chapter 1: [Welcome to the Spring Cloud](./documents/chapter1.md)
***
> Chapter 2: [Building microservices with Spring Boot](./documents/chapter2.md)
***
> Chapter 3: [Building microservices with Spring Cloud Config](./documents/chapter3.md)
>> Quick verify (http://localhost:10003/v1/organizations/442adb6e-fa58-47f3-9ca2-ed1fecdfe86c/licenses/38777179-7094-4200-9d61-edb101c6ea84)
***
> Chapter 4: [Building microservices with Spring Cloud Service Discovery](./documents/chapter4.md)
>> Quick verify (http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/)
***
> Chapter 5: [Client resiliency patterns with Spring Cloud and Netflix](./documents/chapter5.md)
>> Quick verify (http://localhost:10003/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/sleep/1000000)
***
> Chapter 6: [Service Routing with Sping Cloud](./documents/chapter6.md)
>> Quick verify (http://localhost:10005/licensingservice/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a/licenses/)
***
### About HOST:
profile dev configured to connect mysql installed on the local pc, a.k.a connect to: localhost:3306
```
	on windows: localhost == 172.0.0.1
```
profile prod configured to connect mysql installed via docker image, a.k.a connect to: host.docker.internal:10700
```
	on windows: host.docker.internal == windows LAN IP address
```
### About Profiles:
You will be able to override default values configured in bootstrap.yml file with a -D system property.
- If you start licensing service by running jar file directly with command like:
```console
	java -Dspring.profiles.active=prod -jar chapter3-licensing-service.jar
```
Thus, You will connect to mysql instance started via docker image.
- If like this:
```console
	java -Dspring.profiles.active=dev -jar chapter3-licensing-service.jar
```
Then, You will connect to mysql instance installed on your local PC.
### About Docker Command Line: (running with windows powershell)
> Delete all containers:
>> docker rm $(docker ps -a -q)

> Delete all untaged images:
>> docker rmi $(docker images -f "dangling=true" -q)

> Delete all images since another one created: (or "before")
>> docker rmi $(docker images -f "since=mysql" -q) 

> Delete all images with reference
>> docker rmi $(docker images -f "reference=andyhecd/*:*SNAPSHOT" -q) 

> Run mysql image separately
>> docker run --name docker-mysql -dp 10700:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=microservice mysql
>> docker exec -it docker-mysql bash #Get into mysql command line
