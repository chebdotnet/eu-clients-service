package com.eu.client.registration.api;

import com.eu.client.registration.service.country.CountryService;
import com.eu.client.registration.service.country.ShortestPathDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = CountryController.PATH)
@RequiredArgsConstructor
public class CountryController {

    static final String PATH = "api/country";

    private final CountryService countryService;

    @ApiOperation(value = "Search the shortest way between countries", notes = "Please use 'alpha3Code' countries codes like LVA, POL, PRT etc")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    ShortestPathDto getShortestPath(@ApiParam(value = "Start country code") @RequestParam("startCountry") String startCountry,
                                    @ApiParam(value = "Finish country code") @RequestParam("finishCountry") String finishCountry) {

        return countryService.findTheShortestWay(startCountry.toUpperCase(), finishCountry.toUpperCase());

    }

}
