package com.adelahmadi.springit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner is a functional interface that can be used to run code at application startup.
 * It is typically used to execute code after the Spring application context has been initialized.
 * In this case, it is used to load the database with initial data.
 */

@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Override
    public void run(String... args) throws Exception {
        // do something with the database
        // For example, you could load initial data into the database here.
        // This method will be called after the application context is loaded.

        logger.info("DatabaseLoader is running. You can load initial data here.");

        // throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
