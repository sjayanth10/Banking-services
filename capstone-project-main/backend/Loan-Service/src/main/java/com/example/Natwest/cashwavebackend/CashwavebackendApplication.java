package com.example.Natwest.cashwavebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CashwavebackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashwavebackendApplication.class, args);
	}

}
