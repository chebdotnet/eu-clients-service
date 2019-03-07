package com.eu.client.registration.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.AUTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "countries")
@SequenceGenerator(name = "countries_sequence", sequenceName = "countries_seq", allocationSize = 1)
public class Country {

    @Id
    @Column(name = "country_id", nullable = false)
    @GeneratedValue(generator = "countries_sequence", strategy = AUTO)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "population", nullable = false)
    private Long population;

    @Column(name = "area", nullable = false)
    private BigDecimal area;

    @Column(name = "bordering_countries", nullable = false)
    private String borderingCountries;

}
