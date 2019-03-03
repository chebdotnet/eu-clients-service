package com.eu.client.registration.service;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
class ValidationException extends RuntimeException {

    static final String MESSAGE_TEMPLATE = "Registration has been denied. Submitted country code %s is of outside of Europe";

    static final String EMAIL_UNIQUE_MESSAGE_TEMPLATE = "Registration has been denied. Duplicated user with email %s";

    ValidationException(String message) {
        super(message);
    }

}
