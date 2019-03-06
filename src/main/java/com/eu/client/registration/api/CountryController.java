package com.eu.client.registration.api;

import com.eu.client.registration.service.country.CountryService;
import com.eu.client.registration.service.country.ShortestPathDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = CountryController.PATH)
@RequiredArgsConstructor
public class CountryController {

    static final String PATH = "api/country";

    private final CountryService countryService;

    @ApiOperation("Search the shortest way between countries")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    ShortestPathDto getShortestPath(@QueryParam("startCountry") @ApiParam(value = "Start country code") String startCountry,
                                    @QueryParam("finishCountry") @ApiParam(value = "Finish country code") String finishCountry) {

        return countryService.findTheShortestWay(startCountry.toUpperCase(), finishCountry.toUpperCase());

    }

}
