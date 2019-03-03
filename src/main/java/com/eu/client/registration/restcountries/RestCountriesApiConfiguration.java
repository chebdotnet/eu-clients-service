package com.eu.client.registration.restcountries;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.optionals.OptionalDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RestCountriesApiConfiguration {

    private final ObjectMapper objectMapper;

    private final RestCountriesApiProperties restCountriesApiProperties;

    @Bean
    public RestCountriesApi restCountriesApi() {
        return build().target(RestCountriesApi.class, restCountriesApiProperties.getUrl());
    }

    private Feign.Builder build() {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new OptionalDecoder(new JacksonDecoder(objectMapper)))
                .contract(new JAXRSContract());
    }

}
