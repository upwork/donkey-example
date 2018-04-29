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
public class AnimalsResourceClient {
    private final ConnectionSettings connectionSettings;

    public AnimalsResourceClient(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    /**
     * Retrieves all animals in the zoo;
     * @throws RestClientException if communication exception happens
     */
    public AnimalList getAll() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/animals")
                ;
        URI uri = uriBuilder.build()
                .encode()
                .toUri();
        RequestEntity<Void> requestEntity = RequestEntity.get(uri)
                .build();
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        return restTemplate.exchange(requestEntity, AnimalList.class).getBody();
    }

    /**
     * @throws RestClientException if communication exception happens
     */
    public void addAnimal(String type, Animal animal) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/animals")
                .path("/{type}")
                ;
        URI uri = uriBuilder.build()
                .expand(new java.util.HashMap<String, String>() {{
                    put("type", type.toString());
                }})
                .encode()
                .toUri();
        RequestEntity<Animal> requestEntity = RequestEntity.post(uri)
                .body(animal);
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        restTemplate.exchange(requestEntity, Void.class);
    }

    /**
     * @throws InvalidAnimalTypeException if server responds with this donkey-declared exception
     * @throws RestClientException if communication exception happens
     */
    public AnimalList findAnimalsByType(String type) throws InvalidAnimalTypeException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/animals")
                .path("/{type}")
                ;
        URI uri = uriBuilder.build()
                .expand(new java.util.HashMap<String, String>() {{
                    put("type", type.toString());
                }})
                .encode()
                .toUri();
        RequestEntity<Void> requestEntity = RequestEntity.get(uri)
                .build();
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        try {
            return restTemplate.exchange(requestEntity, AnimalList.class).getBody();
        } catch (HttpStatusCodeException httpException) {
            Optional<Integer> errorCode = getDonkeyErrorCode(httpException);
            if (!errorCode.isPresent()) {
                throw httpException;
            }
            if (errorCode.get().equals(InvalidAnimalTypeException.ERROR_CODE) &&
                    InvalidAnimalTypeException.STATUS_CODE == httpException.getRawStatusCode()) {
                throw new InvalidAnimalTypeException();
            }
            throw httpException;
        }
    }

    /**
     * Marks all animals as vaccinated
     * @throws AlreadyVaccinatedException if server responds with this donkey-declared exception
     * @throws RestClientException if communication exception happens
     */
    public void markAsVaccinated(String type, Optional<java.util.List<String>> names) throws AlreadyVaccinatedException {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme(connectionSettings.getScheme())
                .host(connectionSettings.getHost())
                .port(connectionSettings.getPort())
                .path("/animals")
                .path("/{type}/vaccinate")
                ;

        if (names.isPresent()) {
            uriBuilder.queryParam("names", names.get().toArray());
        }

        URI uri = uriBuilder.build()
                .expand(new java.util.HashMap<String, String>() {{
                    put("type", type.toString());
                }})
                .encode()
                .toUri();
        RequestEntity<Void> requestEntity = RequestEntity.post(uri)
                .build();
        RestTemplate restTemplate = connectionSettings.getRestTemplate();
        try {
            restTemplate.exchange(requestEntity, Void.class);
        } catch (HttpStatusCodeException httpException) {
            Optional<Integer> errorCode = getDonkeyErrorCode(httpException);
            if (!errorCode.isPresent()) {
                throw httpException;
            }
            if (errorCode.get().equals(AlreadyVaccinatedException.ERROR_CODE) &&
                    AlreadyVaccinatedException.STATUS_CODE == httpException.getRawStatusCode()) {
                throw new AlreadyVaccinatedException();
            }
            throw httpException;
        }
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