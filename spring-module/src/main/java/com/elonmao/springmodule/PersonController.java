package com.elonmao.springmodule;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonController(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void encodePassword(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Person> getPerson(@PathVariable String username) {
        Person person = personRepository.findById(username).orElse(null);
        if (person != null) {
            person.setPassword(null);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> updatePerson(@PathVariable String username, @RequestBody Person person) {
        person.setUsername(username);
        encodePassword(person);
        personRepository.save(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deletePerson(@PathVariable String username) {
        personRepository.deleteById(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
