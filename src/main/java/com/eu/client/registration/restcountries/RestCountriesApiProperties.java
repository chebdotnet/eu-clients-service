package com.eu.client.registration.restcountries;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "countries-api")
class RestCountriesApiProperties {

    private String url;

}
