package org.omar.recipemanagementsystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Intentionally a no-op.
 * Seed data (tags, recipemanagementsystem, users) is now loaded by data.sql at startup
 * so this class no longer reads the JSON files or registers users.
 */
@Component
public class StartUp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StartUp.class);

    @Override
    public void run(String... args) {
        log.info("StartUp: seed data is loaded by data.sql; nothing to do here.");
    }
}
