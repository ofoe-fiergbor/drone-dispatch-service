# Dispatch Management Service

This repository was created to demonstrate a fully fledged REST API service for a Dispatch Management Service.

For our use case the functionality for this service is centered on:
- registering a drone;
- loading a drone with medication items;
- checking loaded medication items for a given drone;
- checking available drones for loading;
- check drone battery level for a given drone;

## Setup, Testing and Running

If you are interested in setting up this project then:

* Clone repository.

        git clone https://gitlab.com/ofoe-fiergbor1/drone-dispatch-service.git
* Build.

        ./gradlew build
* Run tests.

        ./gradlew test

* Run service

        ./gradlew bootRun

* API/Swagger documentation

        http://localhost:8080/openapi/swagger-ui/index.html


## Dependencies
    Java Version = 17
    
    implementation 'org.springdoc:springdoc-openapi-ui:1.6.14'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    runtimeOnly 'com.h2database:h2'
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
