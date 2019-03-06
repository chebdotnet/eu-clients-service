package com.eu.client.registration.api;


import com.eu.client.registration.service.client.ClientDto;
import com.eu.client.registration.service.client.ClientService;
import com.eu.client.registration.service.client.CountryDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = UserController.PATH, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    static final String PATH = "api/user";

    private final ClientService clientService;

    @ApiOperation("Expose client data")
    @GetMapping(value = "/info")
    @ResponseStatus(OK)
    ClientDto getUserBasicInfo() {
        return clientService.getUserBasicInfo();
    }

    @ApiOperation("Detailed data about users country captured from https://restcountries.eu/ at the time of registration")
    @GetMapping(value = "/country")
    @ResponseStatus(OK)
    CountryDto getClientCountryInfo() {
        return clientService.getClientCountryInfo();
    }

}
