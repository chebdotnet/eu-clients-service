package com.eu.client.registration.service.client;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
class ClientNotFoundException extends RuntimeException {

    static final String MESSAGE_TEMPLATE = "Client with email %s wasn't found";

    ClientNotFoundException(String message) {
        super(message);
    }

}


