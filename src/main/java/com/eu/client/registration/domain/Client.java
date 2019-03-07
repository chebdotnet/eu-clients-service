package com.eu.client.registration.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
@SequenceGenerator(name = "clients_sequence", sequenceName = "clients_seq", allocationSize = 1)
public class Client {

    @Id
    @Column(name = "client_id", nullable = false)
    @GeneratedValue(generator = "clients_sequence", strategy = AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "country", nullable = false)
    private String countryCode;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "country_id")
    private Country country;

}