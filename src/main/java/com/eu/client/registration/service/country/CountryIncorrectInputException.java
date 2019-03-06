package com.eu.client.registration.service.country;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class CountryIncorrectInputException extends IllegalArgumentException {

    private static final String ERROR_MESSAGE = "Seems the input data is invalid. Please re-check it!";

    CountryIncorrectInputException() {
        super(ERROR_MESSAGE);
    }

}
