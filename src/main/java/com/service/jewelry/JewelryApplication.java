package com.service.jewelry;

import com.service.jewelry.model.dto.UserRegistrationDto;
import com.service.jewelry.service.UserServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class JewelryApplication {
	@Autowired
	UserServiceImpl userService;

	public static void main(String[] args) {SpringApplication.run(JewelryApplication.class, args);

	}

	@Bean
	InitializingBean sendDatabase() {
		return () -> userService.saveAdmin(UserRegistrationDto.builder()
						.email("admin@admin.com")
						.firstName("Admin")
						.lastName("Admin")
						.password("admin228")
				.build());
	}
}
