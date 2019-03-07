package com.eu.client.registration.service.client;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientRepository;
import com.eu.client.registration.domain.Country;
import com.eu.client.registration.domain.CountryRepository;
import com.eu.client.registration.restcountries.CountryBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import com.eu.client.registration.service.client.converters.ToClient;
import com.eu.client.registration.service.client.converters.ToClientDto;
import com.eu.client.registration.service.client.validators.ClientRegisterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.requireNonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ToClient toClient;

    private final ToClientDto toClientDto;

    private final ClientRepository repository;

    private final CountryRepository countryRepository;

    private final List<ClientRegisterValidator> validators;

    private final RestCountriesApi restCountriesApi;

    @Transactional
    public void register(ClientBean bean) {
        validators.forEach(validator -> validator.validate(bean));
        Country country = captureCountry(bean.getCountryCode());


        Client client = toClient.convert(bean);
        requireNonNull(client).setCountry(country);
        repository.save(requireNonNull(client));
    }

    private Country captureCountry(String countryCode) {
        return countryRepository.findByCode(countryCode).orElseGet(() -> {
            CountryBean countryBean = restCountriesApi.fetchCountryByCountryCode(countryCode);
            Country country = Country.builder()
                    .code(countryCode)
                    .area(countryBean.getArea())
                    .borderingCountries(String.join(";", countryBean.getBorders()))
                    .population(countryBean.getPopulation())
                    .build();
            return countryRepository.save(country);

        });
    }

    public ClientDto getUserBasicInfo() {
        Client client = findAuthClient();
        return toClientDto.convert(client);
    }

    public CountryDto getClientCountryInfo() {
        Client client = findAuthClient();

        Country country = client.getCountry();
        return CountryDto.builder()
                .area(country.getArea())
                .population(country.getPopulation())
                .borderingCountries(List.of(country.getBorderingCountries().split(";")))
                .build();
    }

    private Client findAuthClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return repository.findByEmail(auth.getName()).orElseThrow(() -> new ClientNotFoundException(String.format(ClientNotFoundException.MESSAGE_TEMPLATE, auth.getName())));
    }

}
