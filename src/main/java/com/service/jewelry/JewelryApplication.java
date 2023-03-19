package com.service.jewelry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class JewelryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelryApplication.class, args);
	}

}
