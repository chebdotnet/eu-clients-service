package com.eu.client.registration.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients_countries")
@SequenceGenerator(name = "clients_countries_sequence", sequenceName = "clients_countries_seq", allocationSize = 1)
public class ClientCountry {

    @Id
    @Column(name = "clients_countries_id", nullable = false)
    @GeneratedValue(generator = "clients_countries_sequence", strategy = AUTO)
    private Long id;

    @Column(name = "population")
    private Long population;

    @Column(name = "area")
    private BigDecimal area;

    @Column(name = "bordering_countries")
    private String borderingCountries;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    private Client client;

}
