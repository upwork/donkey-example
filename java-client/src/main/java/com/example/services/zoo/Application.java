package com.example.services.zoo;

import com.example.zoo.messageconverter.ThriftJsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.services.zoo.exception.AlreadyVaccinatedException;
import com.example.services.zoo.thrift.Animal;
import com.example.services.zoo.thrift.AnimalList;
import com.example.services.zoo.thrift.Person;
import com.example.services.zoo.thrift.PersonList;
import java.util.Arrays;
import java.util.Optional;


@SpringBootApplication
public class Application implements CommandLineRunner{

    @Autowired
    private AnimalsResourceClient animalsResourceClient;

    @Autowired
    private StaffResourceClient staffResourceClient;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ConnectionSettings buildZooServiceConnectionSettings(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(400)
                .setReadTimeout(7_000)
                .messageConverters(new ThriftJsonMessageConverter())
                .build();
        return new ConnectionSettings("http", "localhost", 8000, restTemplate);
    }

    @Override
    public void run(String... args) {
        // -- Animals resource

        System.out.println("Adding some animals...");
        animalsResourceClient.addAnimal("donkey", new Animal("Daisy", "donkey", false));
        animalsResourceClient.addAnimal("sheep", new Animal("Polly", "sheep", false));
        animalsResourceClient.addAnimal("donkey", new Animal("Johny", "donkey", false));

        System.out.println("Loading all animals in the registry...");
        AnimalList animals = animalsResourceClient.getAll();
        System.out.println(animals.getAnimals().size() + " animals found");

        System.out.println("Vaccinating the donkeys...");
        try {
            animalsResourceClient.markAsVaccinated("donkey", Optional.of(Arrays.asList("Daisy", "Johny")));
        } catch (AlreadyVaccinatedException exception) {
            System.out.println("This is not supposed to happen, unless you run the demo multiple times");
        }

        System.out.println("Attempting to vaccinate Johny once again...");
        try {
            animalsResourceClient.markAsVaccinated("donkey", Optional.of(Arrays.asList("Daisy")));
        } catch (AlreadyVaccinatedException exception) {
            System.out.println("Caught already vaccinated exception, as expected");
        }

        System.out.println("Looking up animals by type...");
        AnimalList sheep = animalsResourceClient.findAnimalsByType("sheep");
        System.out.println(sheep.getAnimals().size() + " sheep found");

        // -- Staff resource

        System.out.println("Hiring a guard...");
        staffResourceClient.hireGuard(new Person("Karl", "Smith", "7767", 10));

        System.out.println("And two janitors...");
        staffResourceClient.hireJanitor(new Person("Alex", "Peterson", "6783623", 5));
        staffResourceClient.hireJanitor(new Person("John", "Smith", "7987234", 15));

        System.out.println("Searching for employees with last name Smith, reading their IDs:");
        staffResourceClient.getAllEmployees(Optional.empty(), Optional.of("Smith"))
                .getPersons().stream().map(Person::getGovernmentId).forEach(System.out::println);


        System.out.println("Letting one of the janitors go...");
        staffResourceClient.terminateEmployee("7987234");
    }
}
