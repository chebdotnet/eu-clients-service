package com.eu.client.registration.services;

import com.eu.client.registration.domain.Client;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToClient implements Converter<ClientBean, Client> {

    @Override
    public Client convert(ClientBean request) {
        return Client.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .country(request.getCountry())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
