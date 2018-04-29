<?php

namespace Services\Zoo\Service;

use Services\Zoo\Exception;
use Services\Zoo\Thrift;

class AnimalsServiceImpl implements AnimalsService
{
    use DataStore;

    public function __construct()
    {
        $this->initDataStore();
    }

    /**
     * Retrieves all animals in the zoo;
     *
     * @return Thrift\AnimalList|null
     */
    public function getAll(): ?Thrift\AnimalList
    {
        return new Thrift\AnimalList(["animals" => $this->getAnimals()]);
    }

    /**
     *
     * @param string $type
     * @param Thrift\Animal|null $animal
     */
    public function addAnimal(string $type, ?Thrift\Animal $animal): void
    {
        $animals = $this->getAnimals();
        $animals[] = $animal;
        $this->saveAnimals($animals);
    }

    /**
     *
     * @param string $type
     *
     * @return Thrift\AnimalList|null
     *
     * @throws Exception\InvalidAnimalTypeException
     */
    public function findAnimalsByType(string $type): ?Thrift\AnimalList
    {
        if ($type === "unsupported") {
            throw new Exception\InvalidAnimalTypeException("Unsupported animal type: $type");
        }

        $animals = $this->getAnimals();
        $filteredAnimals = [];
        foreach ($animals as $animal) {
            if ($animal->type === $type) {
                $filteredAnimals[] = $animal;
            }
        }

        return new Thrift\AnimalList(["animals" => $filteredAnimals]);
    }

    /**
     * Marks all animals as vaccinated
     *
     * @param string $type
     * @param string[]|null $names
     *
     * @throws Exception\AlreadyVaccinatedException
     */
    public function markAsVaccinated(string $type, ?array $names): void
    {
        $animals = $this->getAnimals();
        foreach ($animals as $animal) {
            if ($animal->type === $type && (!$names || in_array($animal->name, $names))) {
                if ($animal->vaccinated) {
                    throw new Exception\AlreadyVaccinatedException(
                        "Animal {$animal->name} has already been vaccinated"
                    );
                }
                $animal->vaccinated = true;
            }
        }
        $this->saveAnimals($animals);
    }

    /**
     * @return Thrift\Animal[]
     */
    private function getAnimals(): array
    {
        return $this->dataStore->get("animals", []);
    }

    /**
     * @param Thrift\Animal[] $animals
     */
    private function saveAnimals(array $animals): void
    {
        $this->dataStore->set("animals", $animals);
    }
}