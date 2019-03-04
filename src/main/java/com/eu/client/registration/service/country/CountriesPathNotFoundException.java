package com.eu.client.registration.service.country;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class CountriesPathNotFoundException extends RuntimeException {

    static final String MESSAGE_TEMPLATE = "The way between countries %s and %s not found";

    CountriesPathNotFoundException(String message) {
        super(message);
    }

}
