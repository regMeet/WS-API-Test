# WS-API-Test
This repo contains a rest service with some endpoints for testing purpose.
It uses:
Spring boot
Java 8
Spring Web Services

## Requirements
Maven
Java 8

## Compilation
To Compile project:
```
> mvn clean install
```

To compile it without running the test:
```
> mvn clean install -DskipTests
```

## Running project
To run the web services:
```
> mvn spring-boot:run
```

The services are running in the url: http://localhost:8085/api/test
Being "/test" the endpoint tested

## Postman
If you want to try which are the web services you can import the postman collection which is in the root folder and then try any request after running the web services.

