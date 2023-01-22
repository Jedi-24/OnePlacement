package com.Jedi.OnePlacementServer;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication // beans can be declared here, as this is already a configuration class or make a custom config. class;
public class OnePlacementServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnePlacementServerApplication.class, args);
	}
	@Bean // spring container detects bean,object is ready -> automatically and inject it during auto wiring.
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
