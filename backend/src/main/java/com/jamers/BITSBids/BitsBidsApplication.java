package com.jamers.BITSBids;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BitsBidsApplication {
	public static void main(String[] args) {
		SpringApplication.run(BitsBidsApplication.class, args);
	}
}
