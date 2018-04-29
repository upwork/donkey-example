<?php

require __DIR__ . "/../vendor/autoload.php";

use GuzzleHttp\Client as GuzzleHttpClient;
use Services\Zoo\Client\AnimalsClient;
use Services\Zoo\Client\StaffClient;
use Services\Zoo\Client\ThriftMessageConverter;
use Services\Zoo\Thrift\Animal;
use Services\Zoo\Thrift\AnimalList;
use Services\Zoo\Thrift\Person;
use Services\Zoo\Thrift\PersonList;
use Services\Zoo\Exception\AlreadyVaccinatedException;

$messageConverter = new ThriftMessageConverter();
$httpClient = new GuzzleHttpClient(
    [
        "base_uri" => "localhost:8000",
        "headers" => ["Content-Type" => "application/vnd.apache.thrift.json"]
    ]
);

// -- Animals resource
$animalsClient = new AnimalsClient($httpClient, $messageConverter);

print("Adding some animals...\n");
$animalsClient->addAnimal("donkey", new Animal("Daisy", "donkey", false));
$animalsClient->addAnimal("sheep", new Animal("Polly", "sheep", false));
$animalsClient->addAnimal("donkey", new Animal("Johny", "donkey", false));

print("Loading all animals in the registry...\n");
$animals = $animalsClient->getAll();
print(count($animals->animals) . " animals found\n");

print("Vaccinating the donkeys...\n");
try {
    $animalsClient->markAsVaccinated("donkey", ["Daisy", "Johny"]);
} catch (AlreadyVaccinatedException $exception) {
    print("This is not supposed to happen, unless you run the demo multiple times\n");
}

print("Attempting to vaccinate Johny once again...\n");
try {
    $animalsClient->markAsVaccinated("donkey", ["Daisy"]);
} catch (AlreadyVaccinatedException $exception) {
    print("Caught already vaccinated exception, as expected\n");
}

print("Looking up animals by type...\n");
$sheep = $animalsClient->findAnimalsByType("sheep");
print(count($sheep->animals) . " sheep found\n");

// -- Staff resource
$staffClient = new StaffClient($httpClient, $messageConverter);

print("Hiring a guard...\n");
$staffClient->hireGuard(new Person("Karl", "Smith", "7767", 10));

print("And two janitors...\n");
$staffClient->hireJanitor(new Person("Alex", "Peterson", "6783623", 5));
$staffClient->hireJanitor(new Person("John", "Smith", "7987234", 15));

print("Searching for employees with last name Smith, reading their IDs:\n");
$employees = $staffClient->getAllEmployees(null, "Smith");
foreach($employees->persons as $employee) {
    print($employee->governmentId . "\n");
}

print("Letting one of the janitors go...\n");
$staffClient->terminateEmployee("7987234");