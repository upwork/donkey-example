package com.example.services.zoo;

import com.example.services.zoo.thrift.*;
import com.example.services.zoo.exception.*;

import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.upwork.donkey.example.spring.ServiceGenerator, donkey-example:1.0-SNAPSHOT")
public interface StaffService {
    void hireJanitor(Person janitor);

    void hireGuard(Person guard);

    PersonList getAllEmployees(Optional<String> firstName, Optional<String> lastName);

    void terminateEmployee(String id);
}