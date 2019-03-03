package com.eu.client.registration.service.validators;

import com.eu.client.registration.service.ClientBean;

@FunctionalInterface
public interface ClientRegisterValidator {

    void validate(ClientBean bean);

}
