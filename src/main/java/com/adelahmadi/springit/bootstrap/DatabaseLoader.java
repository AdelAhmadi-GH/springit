package com.adelahmadi.springit.bootstrap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.domain.Role;
import com.adelahmadi.springit.domain.User;
import com.adelahmadi.springit.repository.CommentRepository;
import com.adelahmadi.springit.repository.LinkRepository;
import com.adelahmadi.springit.repository.RoleRepository;
import com.adelahmadi.springit.repository.UserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {
        private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

        private final LinkRepository linkRepository;
        private final CommentRepository commentRepository;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;

        public DatabaseLoader(LinkRepository linkRepository, CommentRepository commentRepository,
                        UserRepository userRepository, RoleRepository roleRepository) {
                // Constructor injection for repositories
                this.linkRepository = linkRepository;
                this.commentRepository = commentRepository;
                this.userRepository = userRepository;
                this.roleRepository = roleRepository;
        }

        @Override
        public void run(String... args) {
                addUsersAndroles(); // Ensure users and roles are created before links
                addSomeLinks(); // Load sample links into the database
        }

        private void addSomeLinks() {
                // Sample data to load into the database, Email and URL are just examples
                // You can replace these with actual data or leave it empty to start fresh.
                // This is just an example, you can modify it as needed
                Map<String, String> links = new HashMap<>();
                links.put("Securing Spring Boot APIs and SPAs with OAuth 2.0",
                                "https://auth0.com/blog/securing-spring-boot-apis-and-spas-with-oauth2/?utm_source=reddit&utm_medium=sc&utm_campaign=springboot_spa_securing");
                links.put(
                                "Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub",
                                "https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
                links.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)",
                                "https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
                links.put(
                                "Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub",
                                "https://www.opencodez.com/java/send-encrypted-email-using-java.htm");
                links.put("Build a Secure Progressive Web App With Spring Boot and React",
                                "https://dzone.com/articles/build-a-secure-progressive-web-app-with-spring-boo");
                links.put("Building Your First Spring Boot Web Application - DZone Java",
                                "https://dzone.com/articles/building-your-first-spring-boot-web-application-ex");
                links.put("Building Microservices with Spring Boot Fat (Uber) Jar",
                                "https://jelastic.com/blog/building-microservices-with-spring-boot-fat-uber-jar/");
                links.put("Spring Cloud GCP 1.0 Released",
                                "https://cloud.google.com/blog/products/gcp/calling-java-developers-spring-cloud-gcp-1-0-is-now-generally-available");
                links.put("Simplest way to Upload and Download Files in Java with Spring Boot - Code to download from Github",
                                "https://www.opencodez.com/uncategorized/file-upload-and-download-in-java-spring-boot.htm");
                links.put("Add Social Login to Your Spring Boot 2.0 app",
                                "https://developer.okta.com/blog/2018/07/24/social-spring-boot");
                links.put("File download example using Spring REST Controller",
                                "https://www.jeejava.com/file-download-example-using-spring-rest-controller/");

                // NOTE: Using Lombok's @RequiredArgsConstructor on Link entity to generate
                // a constructor with (String title, String url) based on @NonNull fields.
                // Without this, 'new Link(k, v)' would cause a "constructor undefined" compile
                // error.
                // Ensure Lombok annotation processing is enabled in the IDE so this works.
                links.forEach((k, v) -> linkRepository.save(new Link(k, v)));
                // we will do something with comments later

                long linkCount = linkRepository.count();
                long commentCount = commentRepository.count();

                logger.info("Database loaded with {} links and {} comments", linkCount, commentCount);
        }

        private void addUsersAndroles() {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                // Generate a secure random password for demonstration; replace with env
                // variable in production
                String rawPassword = "p@ssword!123"; // java.util.UUID.randomUUID().toString
                String secret = "{bcrypt}" + encoder.encode(rawPassword);
                logger.info("Generated secure password for demo users: {}", rawPassword);

                Role userRole = new Role("ROLE_USER");
                roleRepository.save(userRole);
                Role adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);

                User user = new User("user@gmail.com", secret);
                user.setEnabled(true);
                user.addRole(userRole);
                userRepository.save(user);
                logger.info("Created user: {} with roles: {}", user.getEmail(), user.getRoles());

                User admin = new User("admin@gmail.com", secret);
                admin.setEnabled(true);
                admin.addRole(adminRole);
                userRepository.save(admin);
                logger.info("Created user: {} with roles: {}", admin.getEmail(), admin.getRoles());

                User master = new User("master@gmail.com", secret);
                master.setEnabled(true);
                master.addRoles(new HashSet<>(Arrays.asList(userRole, adminRole)));
                userRepository.save(master);
                logger.info("Created user: {} with roles: {}", master.getEmail(), master.getRoles());

                logger.info("Database loaded with {} users and {} roles",
                                userRepository.count(), roleRepository.count());
        }

}
