package com.eu.client.registration.service.validators;

import com.eu.client.registration.restcountries.RegionBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import com.eu.client.registration.service.ClientBean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.eu.client.registration.service.validators.ValidationException.MESSAGE_TEMPLATE;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
class ClientCountryValidator implements ClientRegisterValidator {

    private static final String REGION_EUROPE = "Europe";

    private final RestCountriesApi restCountriesApi;

    @Override
    public void validate(ClientBean bean) {
        RegionBean regionBean = restCountriesApi.fetchRegionyByCountryCode(bean.getCountry());
        if (!REGION_EUROPE.equals(regionBean.getRegion())) {
            throw new ValidationException(format(MESSAGE_TEMPLATE, bean.getCountry()));
        }
    }

}
