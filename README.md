# microservice-spring-andy
All the practices are relevant to book Manning Spring Micro-services in Action 2017.6
### Build and Run all images:
- Step 1: At the root folder of this project, run command *mvn clean package*
- Step 2: Continue to run *docker-compose up*
- Step 3: Access each of service below to verify every chapter.
***
> Chapter 1: [Welcome to the cloud, Spring](https://github.com/andyhecd/microservice-spring-andy/tree/master/chapter1)
>> Quick verify (http://localhost:10701/hello/andy/hee)
***
> Chapter 2: [Building microservices with Spring Boot](https://github.com/andyhecd/microservice-spring-andy/tree/master/chapter2-licensing-service)
>> Quick verify (http://localhost:10702/v1/organizations/sapibsocd/licenses/csccn_andy)
***
> Chapter 3: [Building microservices with Spring Cloud Config](https://github.com/andyhecd/microservice-spring-andy/tree/master/chapter3-licensing-service)
>> Quick verify (http://localhost:11703/v1/organizations/442adb6e-fa58-47f3-9ca2-ed1fecdfe86c/licenses/38777179-7094-4200-9d61-edb101c6ea84)
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
For example:
- You start mysql and configuration server via docker images following steps mentioned above;
- If you start licensing service by running jar file directly with command like:
```
	java -Dspring.profiles.active=prod -jar chapter3-licensing-service.jar
```
Thus, You will connect to mysql instance started via docker image.
- If like this:
```
	java -Dspring.profiles.active=dev -jar chapter3-licensing-service.jar
```
Then, You will connect to mysql instance installed on your local PC.
### About Docker Command Line: (running with windows powershell)
> Delete all containers:
>> docker rm $(docker ps -a -q)
> Delete all untaged images:
>> docker rmi $(docker images -f "dangling=true" -q)
