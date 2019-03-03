package com.eu.client.registration.api;


import com.eu.client.registration.services.ClientBean;
import com.eu.client.registration.services.ClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping(value = ClientController.PATH)
@RequiredArgsConstructor
public class ClientController {

    static final String PATH = "api/clients";

    private final ClientService clientService;

    @ApiOperation("Add client")
    @PostMapping(consumes = "application/json")
    @ResponseStatus(CREATED)
    void addClient(@RequestBody @Valid ClientBean request){
        clientService.register(request);
    }

    @ApiOperation("Expose client data")
    @GetMapping(produces = "application/json", value = "{clientId}")
    @ResponseStatus(OK)
    ClientDto getClient(@PathVariable("clientId") @ApiParam(value = "Client identifier") Long clientId){
        return clientService.getClient(clientId);
    }

}
