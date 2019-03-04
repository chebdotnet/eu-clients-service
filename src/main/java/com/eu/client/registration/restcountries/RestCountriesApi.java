package com.eu.client.registration.restcountries;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public interface RestCountriesApi {

    @GET
    @Path("/alpha/{code}?fields=region")
    @Consumes(APPLICATION_JSON)
    RegionBean fetchRegionByCountryCode(@PathParam("code") String code);

    @GET
    @Path("/alpha/{code}?fields=population;area;borders")
    @Consumes(APPLICATION_JSON)
    CountryBean fetchCountryByCountryCode(@PathParam("code") String code);

    @GET
    @Path("/all")
    @Consumes(APPLICATION_JSON)
    List<CountryBean> fetchAllCountries();

}
