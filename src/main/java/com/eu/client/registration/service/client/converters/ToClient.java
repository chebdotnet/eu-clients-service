package com.eu.client.registration.service.client.converters;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.service.client.ClientBean;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@SuppressWarnings("NullableProblems")
@Component
@RequiredArgsConstructor
public class ToClient implements Converter<ClientBean, Client> {

    private final BCryptPasswordEncoder encoder;

    @Override
    public Client convert(ClientBean request) {
        return Client.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .country(request.getCountry())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();
    }

}
