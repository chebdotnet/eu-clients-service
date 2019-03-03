package com.eu.client.registration.services;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientBean {

    private String name;

    private String surname;

    private String country;

    private String email;

    private String password;

}
