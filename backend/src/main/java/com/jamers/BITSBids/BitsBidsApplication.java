package com.jamers.BITSBids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableAsync
@EnableR2dbcRepositories
public class BitsBidsApplication {
	public static void main(String[] args) {
		SpringApplication.run(BitsBidsApplication.class, args);
	}
}
