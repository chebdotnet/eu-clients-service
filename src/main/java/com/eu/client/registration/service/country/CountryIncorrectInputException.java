package com.eu.client.registration.service.country;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class CountryIncorrectInputException extends IllegalArgumentException {

    CountryIncorrectInputException(String message) {
        super(message);
    }

}
