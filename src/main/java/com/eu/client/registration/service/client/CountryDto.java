package com.eu.client.registration.service.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    Long population;

    BigDecimal area;

    List<String> borderingCountries;

}
