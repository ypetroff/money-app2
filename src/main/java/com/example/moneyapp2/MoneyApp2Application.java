package com.example.moneyapp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyApp2Application {

	public static void main(String[] args) {
		SpringApplication.run(MoneyApp2Application.class, args);
	}

}
