package com.example.services.zoo;

import com.example.services.zoo.thrift.*;
import com.example.services.zoo.exception.*;

import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.upwork.donkey.example.spring.ServiceGenerator, donkey-example:1.0-SNAPSHOT")
public interface AnimalsService {
    AnimalList getAll();

    void addAnimal(String type, Animal animal);

    AnimalList findAnimalsByType(String type) throws InvalidAnimalTypeException;

    void markAsVaccinated(String type, Optional<java.util.List<String>> names) throws AlreadyVaccinatedException;
}