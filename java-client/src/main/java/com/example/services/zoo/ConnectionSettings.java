package com.example.services.zoo;

import org.springframework.web.client.RestTemplate;

import javax.annotation.Generated;

@Generated("com.upwork.donkey.example.spring.ClientGenerator, donkey-example:1.0-SNAPSHOT")
public class ConnectionSettings {
    private final String scheme;
    private final String host;
    private final int port;
    private final RestTemplate restTemplate;

    public ConnectionSettings(String scheme, String host, int port, RestTemplate restTemplate) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.restTemplate = restTemplate;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}