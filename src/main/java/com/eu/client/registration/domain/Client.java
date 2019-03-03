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

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "country")
    private String country;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "client", cascade = ALL, fetch = LAZY)
    private ClientCountry clientCountry;

}