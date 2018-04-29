package com.example.services.zoo;

import com.example.services.zoo.thrift.Person;
import com.example.services.zoo.thrift.PersonList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This code serves as an example, so the operations aren't thread safe and shouldn't be called simultaneously
 */
@Component
public class StaffServiceImpl implements StaffService {
    private final PersonList staff = new PersonList(new ArrayList<>());

    @Override
    public void hireJanitor(Person janitor) {
        staff.addToPersons(janitor);
    }

    @Override
    public void hireGuard(Person guard) {
        staff.addToPersons(guard);
    }

    @Override
    public PersonList getAllEmployees(Optional<String> firstName, Optional<String> lastName) {
        return new PersonList(
                staff.getPersons().stream()
                        .filter(
                                person -> matchesFilter(firstName, person.getFirstName()) &&
                                        matchesFilter(lastName, person.getLastName())
                        )
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void terminateEmployee(String id) {
        Iterator<Person> iterator = staff.getPersonsIterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (Objects.equals(person.getGovernmentId(), id)) {
                iterator.remove();
                return;
            }
        }
    }

    private boolean matchesFilter(Optional<String> filter, String value) {
        if (!filter.isPresent()) {
            return true;
        }
        return Objects.equals(filter.get(), value);
    }
}
