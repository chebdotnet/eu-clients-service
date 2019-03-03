package com.eu.client.registration.service;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.eu.client.registration.service.ClientNotFoundException.MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ToClient toClient;

    private final ToClientDto toClientDto;

    private final ClientRepository repository;

    private final ClientCountryValidator validator;

    @Transactional
    public void register(ClientBean bean) {
        validator.validate(bean.getCountry());
        Client client = toClient.convert(bean);
        repository.save(client);
    }

    public ClientDto getClient(Long clientId) {
        Client client = repository.findById(clientId).orElseThrow(() -> new ClientNotFoundException(String.format(MESSAGE_TEMPLATE, clientId)));
        return toClientDto.convert(client);
    }

}
