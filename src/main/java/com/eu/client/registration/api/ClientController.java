package com.eu.client.registration.api;

import com.eu.client.registration.service.client.ClientBean;
import com.eu.client.registration.service.client.ClientService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

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
    void addClient(@RequestBody @Valid ClientBean request) {
        clientService.register(request);
    }

}
