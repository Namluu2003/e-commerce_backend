package com.poly.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling  // Thêm annotation này
public class DatnBackendSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatnBackendSpringApplication.class, args);
	}

}
