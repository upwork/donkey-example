package com.example.services.zoo;

import com.example.services.zoo.thrift.*;
import com.example.services.zoo.exception.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Generated;
import java.util.Optional;

@RestController
@RequestMapping("/animals")
@Generated("com.upwork.donkey.example.spring.ServiceGenerator, donkey-example:1.0-SNAPSHOT")
public class AnimalsResource {
    private final AnimalsService service;

    public AnimalsResource(AnimalsService service) {
        this.service = service;
    }

    /**
     * Retrieves all animals in the zoo;
     */
    @RequestMapping(method = RequestMethod.GET)
    public AnimalList getAll() {
        return service.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{type}")
    public void addAnimal(@PathVariable("type") String type, @RequestBody Animal animal) {
        service.addAnimal(type, animal);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{type}")
    public AnimalList findAnimalsByType(@PathVariable("type") String type) throws InvalidAnimalTypeException {
        return service.findAnimalsByType(type);
    }

    /**
     * Marks all animals as vaccinated
     */
    @RequestMapping(method = RequestMethod.POST, path = "/{type}/vaccinate")
    public void markAsVaccinated(@PathVariable("type") String type, @RequestParam(name = "names") Optional<java.util.List<String>> names) throws AlreadyVaccinatedException {
        service.markAsVaccinated(type, names);
    }

    @ExceptionHandler(ZooException.class)
    private ResponseEntity<Void> handleDonkeyException(ZooException e) {
        return ResponseEntity.status(e.getStatusCode())
                .header("vnd.error", String.valueOf(e.getErrorCode()))
                .build();
    }
}