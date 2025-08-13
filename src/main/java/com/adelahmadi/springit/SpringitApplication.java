package com.adelahmadi.springit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class SpringitApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringitApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringitApplication.class, args);
		logger.info("The Application \"Springit\" has started successfully");
	}

}
