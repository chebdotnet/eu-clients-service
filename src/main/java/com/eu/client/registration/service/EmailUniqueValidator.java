package com.eu.client.registration.service;

import com.eu.client.registration.domain.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.eu.client.registration.service.ValidationException.EMAIL_UNIQUE_MESSAGE_TEMPLATE;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class EmailUniqueValidator {

    private final ClientRepository repository;

    public void validate(String email) {
        repository.findByEmail(email).ifPresent(client -> {
            throw new ValidationException(format(EMAIL_UNIQUE_MESSAGE_TEMPLATE, email));
        });
    }
}
