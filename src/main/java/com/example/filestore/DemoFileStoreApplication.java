package com.example.filestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages={"com.example.filestore"})
@EnableJpaAuditing
public class DemoFileStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoFileStoreApplication.class, args);
	}

}
