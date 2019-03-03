package com.eu.client.registration.service;

import com.eu.client.registration.domain.Client;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToClientDto implements Converter<Client, ClientDto> {

    @Override
    public ClientDto convert(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .surname(client.getSurname())
                .country(client.getCountry())
                .email(client.getEmail())
                .build();
    }

}
