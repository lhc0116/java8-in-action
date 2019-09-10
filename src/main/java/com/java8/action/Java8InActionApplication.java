package com.java8.action;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Java8InActionApplication {

	public static void main(String[] args) {
		SpringApplication.run(Java8InActionApplication.class, args);
	}

}
