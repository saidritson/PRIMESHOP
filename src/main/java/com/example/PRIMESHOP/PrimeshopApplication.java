package com.example.PRIMESHOP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "Dominio")
public class PrimeshopApplication {



	public static void main(String[] args) {
		SpringApplication.run(PrimeshopApplication.class, args);
	}

}


