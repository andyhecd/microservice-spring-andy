# Chapter 2: Building microservices with Spring Boot
Chapter 2 focuses on what exactly is a microservice and goes into more detail on how to build a microservice using Spring Boot. This chapter in this code focuses on building a single service called the licensing service. After you have compiled and started the code you should have a service called the licensing service up and running. (This maven project doesn't include this imeplementation)

## Chapter 1 Summary
- Microservice-based architectures have these characteristics:
   + **Constrained**: Microservices have a single set of responsibilities and are narrow in scope. Microservices embrace the UNIX philosophy that an application is nothing more than a collection of services where each service does one thing and does that one thing really well.
   + **Loosely coupled**: A microservice-based application is a collection of small services that only interact with one another through a non–implementation specific interface using a non-proprietary invocation protocol (for example, HTTP and REST). As long as the interface for the service doesn’t change, the owners of the microservice have more freedom to make modifications to the service than in a traditional application architecture.
   + **Abstracted**: Microservices completely own their data structures and data sources. Data owned by a microservice can only be modified by that service. Access control to the database holding the microservice’s data can be locked down to only allow the service access to it.
   + **Independent**: Each microservice in a microservice application can be compiled and deployed independently of the other services used in the application. This means changes can be isolated and tested much more easily than with a more heavily interdependent, monolithic application.
- To be successful with microservices, you need to integrate in the architect’s, software developer’s, and DevOps’ perspectives.
- Microservices, while a powerful architectural paradigm, have their benefits and tradeoffs. Not all applications should be microservice applications.
- From an architect’s perspective, microservices are small, self-contained, and distributed. Microservices should have narrow boundaries and manage a small set of data.
- From a developer’s perspective, microservices are typically built using a RESTstyle of design, with JSON as the payload for sending and receiving data from the service.
- Spring Boot is the ideal framework for building microservices because it lets you build a REST-based JSON service with a few simple annotations.
- From a DevOp’s perspective, how a microservice is packaged, deployed, and monitored are of critical importance.
- Out of the box, Spring Boot allows you to deliver a service as a single executable JAR file. An embedded Tomcat server in the producer JAR file hosts the service.
- Spring Actuator, which is included with the Spring Boot framework, exposes information about the operatio.