package com.elonmao.springmodule;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JPAUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public JPAUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findById(username).orElse(null);
        if (person == null) {
            throw new UsernameNotFoundException(username);
        }
        return person.buildUser();
    }
}
