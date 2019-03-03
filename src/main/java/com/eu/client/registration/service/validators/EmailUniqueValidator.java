package com.eu.client.registration.service.validators;

import com.eu.client.registration.domain.ClientRepository;
import com.eu.client.registration.service.ClientBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.eu.client.registration.service.validators.ValidationException.EMAIL_UNIQUE_MESSAGE_TEMPLATE;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
class EmailUniqueValidator implements ClientRegisterValidator {

    private final ClientRepository repository;

    @Override
    public void validate(ClientBean bean) {
        repository.findByEmail(bean.getEmail()).ifPresent(client -> {
            throw new ValidationException(format(EMAIL_UNIQUE_MESSAGE_TEMPLATE, bean.getEmail()));
        });
    }

}
