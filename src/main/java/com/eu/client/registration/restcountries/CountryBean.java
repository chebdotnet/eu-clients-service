package com.eu.client.registration.restcountries;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CountryBean {

    Long population;

    BigDecimal area;

    List<String> borders;
}
