package com.eu.client.registration.service;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientCountry;
import com.eu.client.registration.domain.ClientRepository;
import com.eu.client.registration.restcountries.CountryBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import com.eu.client.registration.service.validators.ClientRegisterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.eu.client.registration.service.ClientNotFoundException.MESSAGE_TEMPLATE;

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

        Client savedClient = repository.save(client);

        CountryBean countryBean = restCountriesApi.fetchCountryByCountryCode(bean.getCountry());
        ClientCountry clientCountry = ClientCountry.builder()
                .area(countryBean.getArea())
                .borderingCountries(String.join(";", countryBean.getBorders()))
                .population(countryBean.getPopulation())
                .client(client)
                .build();

        savedClient.setClientCountry(clientCountry);
    }


    public ClientDto getClient(Long clientId) {
        Client client = repository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(String.format(MESSAGE_TEMPLATE, clientId)));
        return toClientDto.convert(client);
    }

    public CountryDto getClientCountry(Long clientId) {
        Client client = repository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(String.format(MESSAGE_TEMPLATE, clientId)));
        ClientCountry clientCountry = client.getClientCountry();
        return CountryDto.builder()
                .area(clientCountry.getArea())
                .population(clientCountry.getPopulation())
                .borderingCountries(Arrays.asList(clientCountry.getBorderingCountries().split(";")))
                .build();
    }

}
