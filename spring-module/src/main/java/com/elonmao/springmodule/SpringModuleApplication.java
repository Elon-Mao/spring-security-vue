package com.elonmao.springmodule;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringModuleApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(PersonRepository personRepository) {
		return (args) -> {
			if (personRepository.existsById("admin")) {
				return;
			}
			Person admin = new Person();
			admin.setUsername("admin");
			admin.setPassword("$2a$10$NcgIGyHpES0Iwv4UgaVzdejBEvESXwqw/cLnnTjzZyj9dzU6HCLvG");
			admin.setRoles("admin");
			personRepository.save(admin);
		};
	}
}
