package com.Jedi.OnePlacementServer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication // beans can be declared here, as this is already a configuration class or make a custom config. class;
public class OnePlacementServerApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(OnePlacementServerApplication.class, args);
	}
	@Bean // spring container detects bean,object is ready -> automatically and inject it during auto wiring.
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hehe" + this.passwordEncoder.encode("Jedi2404#"));
	}
}
