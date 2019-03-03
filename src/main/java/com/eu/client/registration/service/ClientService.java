package com.eu.client.registration.service;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientCountry;
import com.eu.client.registration.domain.ClientRepository;
import com.eu.client.registration.restcountries.CountryBean;
import com.eu.client.registration.restcountries.RestCountriesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.eu.client.registration.service.ClientNotFoundException.MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ToClient toClient;

    private final ToClientDto toClientDto;

    private final ClientRepository repository;

    private final ClientCountryValidator validator;

    private final EmailUniqueValidator emailUniqueValidator;

    private final RestCountriesApi restCountriesApi;

    @Transactional
    public void register(ClientBean bean) {
        validator.validate(bean.getCountry());
        emailUniqueValidator.validate(bean.getEmail());

        Client client = toClient.convert(bean);

        CountryBean countryBean = restCountriesApi.fetchCountryByCountryCode(bean.getCountry());
        ClientCountry clientCountry = ClientCountry.builder()
                .area(countryBean.getArea())
                .borderingCountries(String.join(";", countryBean.getBorders()))
                .population(countryBean.getPopulation())
                .client(client)
                .build();

        client.setClientCountry(clientCountry);
        repository.save(client);
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
