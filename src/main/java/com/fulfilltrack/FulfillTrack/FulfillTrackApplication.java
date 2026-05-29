package com.fulfilltrack.FulfillTrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FulfillTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(FulfillTrackApplication.class, args);
	}

}
