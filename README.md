[Donkey](http://github.com/upwork/donkey-core) is DSL for defining RESTful services and code generation tool. If you are familiar with [Apache Thrift](https://thrift.apache.org/), Donkey provides similar toolset, but for RESTful services.

This project demonstrates usage of Donkey for designing and creating services and clients built on top of Java's popular [Spring](https://spring.io/) framework and PHP's [Symfony 4](https://symfony.com/4).

## IDL files

You can use this project with any IDL to design your entire topology of RESTful microservices architecture. We have included [a sample IDL file](zoo.donkey.idl) that defines a service for managing a Zoo. There are two groups of resources: for managing animals in the zoo and work with staff records:

```
serviceName zoo

namespace java com.example.services.zoo
namespace php Services.Zoo

import namespace java com.example.services.zoo.thrift
import namespace php Services.Zoo.Thrift

exception InvalidAnimalTypeException 1
exception AlreadyVaccinatedException 2

resource Animals "/animals" {

    ## Retrieves all animals in the zoo;
    GET AnimalList getAll();

    POST "/{type}" void addAnimal(pathParam string type, requestBody Animal animal);

    GET "/{type}" AnimalList findAnimalsByType(pathParam string type) throws InvalidAnimalTypeException;

    ## Marks all animals as vaccinated
    POST "/{type}/vaccinate" void markAsVaccinated(pathParam string type, queryParam list<string> names) throws AlreadyVaccinatedException;
}

resource Staff "/staff" {

    POST "/janitors" void hireJanitor(requestBody Person janitor);

    POST "/guards" void hireGuard(requestBody Person guard);

    ## Find employees by their first name or last name
    GET PersonList getAllEmployees(queryParam string firstName, queryParam string lastName);

    ## Fire an employee by their ID number
    DELETE "/{id}" void terminateEmployee(pathParam string id);
}
```

Since Donkey does not include a DSL for data types, we used Apache Thrift to [define a couple of structures](zoo.thrift.idl):

```
namespace java com.example.services.zoo.thrift
namespace php Services.Zoo.Thrift

struct Animal {
    1: string name;
    2: string type;
    3: bool vaccinated;
}

struct Person {
    1: string firstName;
    2: string lastName;
    3: string governmentId;
    4: i32 yearsOfExperience;
}

struct PersonList {
    1:  list<Person> persons;
}

struct AnimalList {
    1: list<Animal> animals;
}
```

Thrift is what we use at [Upwork](http://upwork.com) and it makes the definition of contract complete and language-agnostic. You can use anything else for your data, such as a custom JSON hydrator.

## Setting up projects

To build donkey-example project, use:

```
mvn clean install
alias donkey="java -jar target/donkey-example-1.0-SNAPSHOT.jar "
```

### RESTful service built with Spring

1. Create a new Maven project as per https://spring.io/guides/gs/serving-web-content/.
2. Generate REST resources and service interfaces with Donkey as follows:
```
donkey -i zoo.donkey.idl -o spring-service -g com.upwork.donkey.example.spring.ServiceGenerator
```
3. Provide implementation for the services.

Spring auto-wires available resources using "@RestController" annotation so there's nothing else to do. Run the app with ```mvn clean spring-boot:run```.

Alternatively, you can use provided fully functional implementation of the zoo service as a starting point. Go to spring-service and execute:

```
mvn clean spring-boot:run
```
in spring-service directory. It will install the dependencies and start the server.

### RESTful service built with Symfony4

1. Create a new Symfony project as per https://symfony.com/doc/current/setup.html. It will generate entire project structure.
2. Generate REST resources and service interfaces with Donkey as follows:
```
donkey -i zoo.donkey.idl -o symfony4-service -g com.upwork.donkey.example.php.Symfony4Generator
```
3. Provide implementation for the services.

Symfony 4 uses auto-wiring, however depending on the PHP namespace you use in Donkey IDL, you may have to update `services.yaml` and `annotations.yaml`. This only needs to be done once.

Alternatively, you can use provided fully functional implementation of the zoo service as a starting point. Go to symfony4-service directory and execute:

```
composer install
php bin/console server:run
```

## Generating code for the service

Donkey generates the interfaces that ensure the contract as defined in the IDL is implemented. If it's not, the project will not compile (or, in case with PHP, `composer install` will fail).

Implementer of a service does not have to deal with HTTP semantics, controllers, handlers, REST terminology. You only need to provide the business logic, by implementing the interface generated by Donkey.

See examples of generated classes:

### spring:

[AnimalsResource](spring-service/src/main/java/com/example/services/zoo/AnimalsResource.java) Spring's RestController, fully generated by Donkey.

[AnimalsService](spring-service/src/main/java/com/example/services/zoo/AnimalsService.java) - the business service interface that must be implemented.

### symfony4:

[StaffController](symfony4-service/src/Services/Zoo/Controller/StaffController.php) is a regular Symfony controller fully generated by Donkey.

[StaffService](symfony4-service/src/Services/Zoo/Service/StaffService.php) - the business service interface that must be implemented.

Donkey also helps to standardize and enforce correct exception handling by generating exception classes

### spring server-side exceptions

[AlreadyVaccinatedException](spring-service/src/main/java/com/example/services/zoo/exception/AlreadyVaccinatedException.java)

[InvalidAnimalTypeException](spring-service/src/main/java/com/example/services/zoo/exception/InvalidAnimalTypeException.java)

### symfony 4 server-side exceptions

[AlreadyVaccinatedException](symfony4-service/src/Services/Zoo/Exception/AlreadyVaccinatedException.php)

[InvalidAnimalTypeException](symfony4-service/src/Services/Zoo/Exception/InvalidAnimalTypeException.php)

## Client applications

The generators in this project create fully functional clients that can be used in any PHP or JVM program. To showcase usage, we included two command line applications:

Run the [Java command line application](java-client/src/main/java/com/example/services/zoo/Application.java) with
```
mvn clean spring-boot:run
```

Run the [PHP command line application](php-client/src/zoo-client-console.php) with
```
composer install
php src/zoo-client-console.php
```

## LICENSE

Copyright 2018 Upwork Global Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.