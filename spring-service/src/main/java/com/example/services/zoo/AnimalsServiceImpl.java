package com.example.services.zoo;

import com.example.services.zoo.exception.AlreadyVaccinatedException;
import com.example.services.zoo.exception.InvalidAnimalTypeException;
import com.example.services.zoo.thrift.Animal;
import com.example.services.zoo.thrift.AnimalList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This code serves as an example, so the operations aren't thread safe and shouldn't be called simultaneously
 */
@Component
public class AnimalsServiceImpl implements AnimalsService {
    private final AnimalList animals = new AnimalList(new ArrayList<>());

    @Override
    public AnimalList getAll() {
        return animals;
    }

    @Override
    public void addAnimal(String type, Animal animal) {
        animals.addToAnimals(animal);
    }

    @Override
    public AnimalList findAnimalsByType(String type) throws InvalidAnimalTypeException {
        return new AnimalList(
                animals.getAnimals().stream()
                        .filter(animal -> animal.getType().equals(type))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void markAsVaccinated(String type, Optional<List<String>> names) throws AlreadyVaccinatedException {
        List<String> namesList = names.orElse(Collections.emptyList());
        boolean repeatedVaccinationRequested = animals.getAnimals().stream()
                .filter(animal -> animalMatches(animal, type, namesList))
                .anyMatch(Animal::isVaccinated);
        if (repeatedVaccinationRequested) {
            throw new AlreadyVaccinatedException();
        }

        animals.getAnimals().stream()
                .filter(animal -> animalMatches(animal, type, namesList))
                .forEach(animal -> animal.setVaccinated(true));
    }

    private boolean animalMatches(Animal animal, String type, List<String> possibleNamesList) {
        return animal.getType().equals(type) && possibleNamesList.contains(animal.getName());
    }
}
