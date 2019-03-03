package com.eu.client.registration.service;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
class ClientNotFoundException extends RuntimeException {

    static final String MESSAGE_TEMPLATE = "Client with id %s isn't found";

    ClientNotFoundException(String message) {
        super(message);
    }

}


