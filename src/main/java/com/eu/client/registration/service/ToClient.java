package com.eu.client.registration.service;

import com.eu.client.registration.domain.Client;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Component
public class ToClient implements Converter<ClientBean, Client> {

    @Override
    public Client convert(ClientBean request) {
        return Client.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .country(request.getCountry())
                .email(request.getEmail())
                .password(md5DigestAsHex(request.getPassword().getBytes()).toUpperCase())
                .build();
    }

}
