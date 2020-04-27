# microservice-spring-andy
All the practices are relevant to book Manning Spring Micro-services in Action 2017.6

###Chapter 1: Welcome to the cloud, Spring
1.At the root folder of this project, run command *mvn clean package*
	- You will get Spring Boot project build and packaged as configuration - jar;
	- You will be able to see a new docker image generated with name andyhecd/chapter1:0.0.1-SNAPSHOT
2.continue to run command *docker run -p 10701:10701 andyhecd/chapter1:0.0.1-SNAPSHOT* 
3.Open browser and try to access service *http://localhost:10701/hello/andy/hee*
	- You should be able to see the correct response with json 
	>{
	>	message: "Hello andy hee"
	>}
