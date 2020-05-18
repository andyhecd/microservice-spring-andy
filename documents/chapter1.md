# Chapter 1: Welcome to the Spring Cloud
Chapter 1 is an introduction to the book. The only code used in this Chapter is a simple, "helloworld" style service that demonstrates how to build a simple microservice using Spring Boot. (This maven project doesn't include this imeplementation)

## Chapter 1 Summary
- Microservices are extremely small pieces of functionality that are responsible for one specific area of scope.
- No industry standards exist for microservices. Unlike other early web service protocols, microservices take a principle-based approach and align with the concepts of REST and JSON.
- Writing microservices is easy, but fully operationalizing them for production requires additional forethought. We introduced several categories of microservice development patterns, including core development, routing patterns, client resiliency, security, logging, and build/deployment patterns.
- While microservices are language-agnostic, we introduced two Spring frameworks that significantly help in building microservices: Spring Boot and Spring Cloud.
- Spring Boot is used to simplify the building of REST-based/JSON microservices. Its goal is to make it possible for you to build microservices quickly with nothing more than a few annotations.
- Spring Cloud is a collection of open source technologies from companies such as Netflix and HashiCorp that have been “wrapped” with Spring annotations to significantly simplify the setup and configuration of these services.