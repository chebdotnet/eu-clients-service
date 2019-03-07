package com.eu.client.registration.service.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientBean {

    @NotEmpty
    private String name;

    @NotEmpty
    private String surname;

    @NotEmpty
    private String countryCode;

    @Email
    private String email;

    @Size(min = 8)
    @NotEmpty
    private String password;

}
