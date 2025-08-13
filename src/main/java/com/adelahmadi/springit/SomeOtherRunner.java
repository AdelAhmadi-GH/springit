package com.adelahmadi.springit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * SomeOtherRunner is a CommandLineRunner that executes code at application startup.
 * It is typically used to perform additional initialization tasks after the application context has been loaded.
 * In this case, it is used to log a message indicating that it is running.
 */

@Component
@Order(2) // Ensures this runner runs after DatabaseLoader
public class SomeOtherRunner implements CommandLineRunner {
        private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Override
    public void run(String... args) throws Exception {
        // do something with the database
        // For example, you could load initial data into the database here.
        // This method will be called after the application context is loaded.

        logger.info("SomeOtherRunner is running. You can load initial data here.");

        // throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
}
