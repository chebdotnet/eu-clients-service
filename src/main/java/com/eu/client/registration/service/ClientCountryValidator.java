package com.eu.client.registration.service;

import com.eu.client.registration.restcountries.RegionBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.eu.client.registration.service.ValidationException.MESSAGE_TEMPLATE;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
class ClientCountryValidator {

    private static final String REGION_EUROPE = "Europe";

    private final RestCountriesApi restCountriesApi;

    void validate(String countryCode) {
        RegionBean regionBean = restCountriesApi.fetchRegionyByCountryCode(countryCode);
        if (!REGION_EUROPE.equals(regionBean.getRegion())) {
            throw new ValidationException(format(MESSAGE_TEMPLATE, countryCode));
        }
    }

}
