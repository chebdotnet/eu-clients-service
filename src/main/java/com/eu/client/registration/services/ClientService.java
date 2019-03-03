package com.eu.client.registration.services;

import com.eu.client.registration.api.ClientDto;
import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ToClient toClient;

    private final ToClientDto toClientDto;

    private final ClientRepository repository;

    @Transactional
    public void register(ClientBean bean){
        Client client = toClient.convert(bean);
        repository.save(client);
    }

    public ClientDto getClient(Long clientId){
        Client client = repository.findById(clientId).orElseThrow(() -> new RuntimeException("Not found"));
        return toClientDto.convert(client);
    }

}
