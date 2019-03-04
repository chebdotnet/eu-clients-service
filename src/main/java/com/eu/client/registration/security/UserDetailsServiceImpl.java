package com.eu.client.registration.security;

import com.eu.client.registration.domain.Client;
import com.eu.client.registration.domain.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Client> clientOpt = repository.findByEmail(username);

        if (clientOpt.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        Client client = clientOpt.get();


        Set<GrantedAuthority> roles = new HashSet();
        roles.add(new SimpleGrantedAuthority("USER"));

        return new org.springframework.security.core.userdetails.User(client.getEmail(),
                client.getPassword(),
                roles);
    }

}
