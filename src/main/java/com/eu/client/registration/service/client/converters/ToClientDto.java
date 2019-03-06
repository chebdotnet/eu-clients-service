package com.eu.client.registration.service.client.converters;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.service.client.ClientDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ToClientDto implements Converter<Client, ClientDto> {

    @SuppressWarnings("NullableProblems")
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
