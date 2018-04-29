package com.example.services.zoo;

import com.example.services.zoo.thrift.*;
import com.example.services.zoo.exception.*;

import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Generated;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Component
@Generated("com.upwork.donkey.example.spring.ClientGenerator, donkey-example:1.0-SNAPSHOT")
public class StaffResourceClient {
    private final ConnectionSettings connectionSettings;

    public StaffResourceClient(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    /**
     * @throws RestClientException if communication exception happens
     */
    public void hireJanitor(Person janitor) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/staff")
                .path("/janitors")
                ;
        URI uri = uriBuilder.build()
                .encode()
                .toUri();
        RequestEntity<Person> requestEntity = RequestEntity.post(uri)
                .body(janitor);
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        restTemplate.exchange(requestEntity, Void.class);
    }

    /**
     * @throws RestClientException if communication exception happens
     */
    public void hireGuard(Person guard) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/staff")
                .path("/guards")
                ;
        URI uri = uriBuilder.build()
                .encode()
                .toUri();
        RequestEntity<Person> requestEntity = RequestEntity.post(uri)
                .body(guard);
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        restTemplate.exchange(requestEntity, Void.class);
    }

    /**
     * Find employees by their first name or last name
     * @throws RestClientException if communication exception happens
     */
    public PersonList getAllEmployees(Optional<String> firstName, Optional<String> lastName) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/staff")
                ;

        if (firstName.isPresent()) {
            uriBuilder.queryParam("firstName", firstName.get());
        }

        if (lastName.isPresent()) {
            uriBuilder.queryParam("lastName", lastName.get());
        }

        URI uri = uriBuilder.build()
                .encode()
                .toUri();
        RequestEntity<Void> requestEntity = RequestEntity.get(uri)
                .build();
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        return restTemplate.exchange(requestEntity, PersonList.class).getBody();
    }

    /**
     * Fire an employee by their ID number
     * @throws RestClientException if communication exception happens
     */
    public void terminateEmployee(String id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/staff")
                .path("/{id}")
                ;
        URI uri = uriBuilder.build()
                .expand(new java.util.HashMap<String, String>() {{
                    put("id", id.toString());
                }})
                .encode()
                .toUri();
        RequestEntity<Void> requestEntity = RequestEntity.delete(uri)
                .build();
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        restTemplate.exchange(requestEntity, Void.class);
    }

    private Optional<Integer> getDonkeyErrorCode(HttpStatusCodeException httpException) {
        List<String> errorHeaderList = httpException.getResponseHeaders().get("vnd.error");
        if (errorHeaderList == null || errorHeaderList.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.valueOf(errorHeaderList.get(0)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}