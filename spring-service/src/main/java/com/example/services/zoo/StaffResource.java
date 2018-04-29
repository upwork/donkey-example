package com.example.services.zoo;

import com.example.services.zoo.thrift.*;
import com.example.services.zoo.exception.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Generated;
import java.util.Optional;

@RestController
@RequestMapping("/staff")
@Generated("com.upwork.donkey.example.spring.ServiceGenerator, donkey-example:1.0-SNAPSHOT")
public class StaffResource {
    private final StaffService service;

    public StaffResource(StaffService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/janitors")
    public void hireJanitor(@RequestBody Person janitor) {
        service.hireJanitor(janitor);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/guards")
    public void hireGuard(@RequestBody Person guard) {
        service.hireGuard(guard);
    }

    /**
     * Find employees by their first name or last name
     */
    @RequestMapping(method = RequestMethod.GET)
    public PersonList getAllEmployees(@RequestParam(name = "firstName") Optional<String> firstName, @RequestParam(name = "lastName") Optional<String> lastName) {
        return service.getAllEmployees(firstName, lastName);
    }

    /**
     * Fire an employee by their ID number
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void terminateEmployee(@PathVariable("id") String id) {
        service.terminateEmployee(id);
    }

    @ExceptionHandler(ZooException.class)
    private ResponseEntity<Void> handleDonkeyException(ZooException e) {
        return ResponseEntity.status(e.getStatusCode())
                .header("vnd.error", String.valueOf(e.getErrorCode()))
                .build();
    }
}