package com.example.moneyapp2;

import com.example.moneyapp2.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class MoneyApp2Application {

	public static void main(String[] args) {
		SpringApplication.run(MoneyApp2Application.class, args);
	}

}
