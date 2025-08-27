package com.store.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PocQueueStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocQueueStoreApplication.class, args);
	}

}
