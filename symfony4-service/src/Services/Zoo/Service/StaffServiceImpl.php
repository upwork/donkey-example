<?php

namespace Services\Zoo\Service;

use Services\Zoo\Thrift;

class StaffServiceImpl implements StaffService
{
    use DataStore;

    public function __construct()
    {
        $this->initDataStore();
    }

    /**
     *
     * @param Thrift\Person|null $janitor
     */
    public function hireJanitor(?Thrift\Person $janitor): void
    {
        $employees = $this->getEmployees();
        $employees[$janitor->governmentId] = $janitor;
        $this->saveEmployees($employees);
    }

    /**
     *
     * @param Thrift\Person|null $guard
     */
    public function hireGuard(?Thrift\Person $guard): void
    {
        $employees = $this->getEmployees();
        $employees[$guard->governmentId] = $guard;
        $this->saveEmployees($employees);
    }

    /**
     * Find employees by their first name or last name
     *
     * @param string|null $firstName
     * @param string|null $lastName
     *
     * @return Thrift\PersonList|null
     */
    public function getAllEmployees(?string $firstName, ?string $lastName): ?Thrift\PersonList
    {
        $employees = $this->getEmployees();
        $filteredEmployees = [];
        foreach ($employees as $employee) {
            if ((!$firstName || $employee->firstName === $firstName)
                && (!$lastName || $employee->lastName === $lastName)) {
                $filteredEmployees[] = $employee;
            }
        }

        return new Thrift\PersonList(["persons" => $filteredEmployees]);
    }

    /**
     * Fire an employee by their ID number
     *
     * @param string $id
     */
    public function terminateEmployee(string $id): void
    {
        $employees = $this->getEmployees();
        if (isset($employees[$id])) {
            unset($employees[$id]);
        }
        $this->saveEmployees($employees);
    }

    /**
     * @return Thrift\Person[]
     */
    private function getEmployees(): array
    {
        return $this->dataStore->get("employees", []);
    }

    /**
     * @param Thrift\Person[] $employees
     */
    private function saveEmployees(array $employees): void
    {
        $this->dataStore->set("employees", $employees);
    }
}