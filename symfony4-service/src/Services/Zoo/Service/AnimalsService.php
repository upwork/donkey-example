<?php

namespace Services\Zoo\Service;

// Class imports

// Namespace imports
use Services\Zoo\Thrift;
use Services\Zoo\Exception;

/**
 * @package Services\Zoo
 *
 * Generated by com.upwork.donkey.example.php.GeneratorAwareTemplateGroupFactory vdonkey-example:1.0-SNAPSHOT
 */
interface AnimalsService
{
    /**
     * Retrieves all animals in the zoo;
     *
     * @return Thrift\AnimalList|null
     */
    public function getAll(): ?Thrift\AnimalList;

    /**
     *
     * @param string $type
     * @param Thrift\Animal|null $animal
     */
    public function addAnimal(string $type, ?Thrift\Animal $animal): void;

    /**
     *
     * @param string $type
     *
     * @return Thrift\AnimalList|null
     *
     * @throws Exception\InvalidAnimalTypeException
     */
    public function findAnimalsByType(string $type): ?Thrift\AnimalList;

    /**
     * Marks all animals as vaccinated
     *
     * @param string $type
     * @param string[]|null $names
     *
     * @throws Exception\AlreadyVaccinatedException
     */
    public function markAsVaccinated(string $type, ?array $names): void;
}