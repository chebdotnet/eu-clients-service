package com.eu.client.registration.service.client;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientCountry;
import com.eu.client.registration.domain.ClientRepository;
import com.eu.client.registration.restcountries.CountryBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import com.eu.client.registration.service.client.converters.ToClient;
import com.eu.client.registration.service.client.converters.ToClientDto;
import com.eu.client.registration.service.client.validators.ClientRegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ToClient toClient;

    private final ToClientDto toClientDto;

    private final ClientRepository repository;

    private final List<ClientRegisterValidator> validators;

    private final RestCountriesApi restCountriesApi;

    @Transactional
    public void register(ClientBean bean) {
        validators.forEach(validator -> validator.validate(bean));

        Client client = toClient.convert(bean);

        Client savedClient = repository.save(requireNonNull(client));

        CountryBean countryBean = restCountriesApi.fetchCountryByCountryCode(bean.getCountry());
        ClientCountry clientCountry = ClientCountry.builder()
                .area(countryBean.getArea())
                .borderingCountries(String.join(";", countryBean.getBorders()))
                .population(countryBean.getPopulation())
                .client(client)
                .build();

        savedClient.setClientCountry(clientCountry);
    }

    public ClientDto getUserBasicInfo() {
        Client client = findAuthClient();
        return toClientDto.convert(client);
    }

    public CountryDto getClientCountryInfo() {
        Client client = findAuthClient();

        ClientCountry clientCountry = client.getClientCountry();
        return CountryDto.builder()
                .area(clientCountry.getArea())
                .population(clientCountry.getPopulation())
                .borderingCountries(Arrays.asList(clientCountry.getBorderingCountries().split(";")))
                .build();
    }

    private Client findAuthClient() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return repository.findByEmail(auth.getName()).orElseThrow(() -> new ClientNotFoundException(String.format(ClientNotFoundException.MESSAGE_TEMPLATE, auth.getName())));
    }

}
