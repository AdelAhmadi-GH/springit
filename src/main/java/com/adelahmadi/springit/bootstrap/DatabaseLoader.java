package com.adelahmadi.springit.bootstrap;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.adelahmadi.springit.domain.Comment;
import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.domain.Role;
import com.adelahmadi.springit.domain.User;
import com.adelahmadi.springit.service.CommentService;
import com.adelahmadi.springit.service.LinkService;
import com.adelahmadi.springit.service.RoleService;
import com.adelahmadi.springit.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseLoader implements CommandLineRunner {
        private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

        private final LinkService linkService;
        private final CommentService commentService;
        private final UserService userService;
        private final RoleService roleService;
        private final PasswordEncoder passwordEncoder;

        @Value("${app.dev.seed-password:ChangeMe!123}")
        private String rawSeedPassword;
        private static final String USER_EMAIL = "user@gmail.com";
        private static final String ADMIN_EMAIL = "admin@gmail.com";
        private static final String MASTER_EMAIL = "master@gmail.com";

        @Override
        public void run(String... args) {
                addUsersAndroles(); // 1) ensure users/roles exist
                addSomeLinks(); // 2) seed links/comments
        }

        private void addUsersAndroles() {
                String encoded = passwordEncoder.encode(rawSeedPassword);

                Role userRole = roleService.save(new Role("ROLE_USER"));
                Role adminRole = roleService.save(new Role("ROLE_ADMIN"));

                if (!userService.existsByEmail(USER_EMAIL)) {
                        User user = new User();
                        user.setEmail(USER_EMAIL.toLowerCase(Locale.ROOT));
                        user.setPassword(encoded);
                        user.setFirstName("Joe");
                        user.setLastName("User");
                        user.setAlias("joedirt");
                        user.setEnabled(true);
                        user.addRole(userRole.getName());
                        userService.saveUser(user);
                }

                if (!userService.existsByEmail(ADMIN_EMAIL)) {
                        User admin = new User();
                        admin.setEmail(ADMIN_EMAIL.toLowerCase(Locale.ROOT));
                        admin.setPassword(encoded);
                        admin.setFirstName("Joe");
                        admin.setLastName("Admin");
                        admin.setAlias("masteradmin");
                        admin.setEnabled(true);
                        admin.addRole(adminRole.getName());
                        userService.saveUser(admin);
                }

                if (!userService.existsByEmail(MASTER_EMAIL)) {
                        User master = new User();
                        master.setEmail(MASTER_EMAIL.toLowerCase(Locale.ROOT));
                        master.setPassword(encoded);
                        master.setFirstName("Super");
                        master.setLastName("User");
                        master.setAlias("superduper");
                        master.setEnabled(true);
                        master.addRole(userRole.getName());
                        master.addRole(adminRole.getName());
                        userService.saveUser(master);
                }

                logger.info("Database loaded with {} users and {} roles", userService.count(), roleService.count());
        }

        private void addSomeLinks() {
                Map<String, String> links = new LinkedHashMap<>();
                links.put("Securing Spring Boot APIs and SPAs with OAuth 2.0",
                                "https://auth0.com/blog/securing-spring-boot-apis-and-spas-with-oauth2/?utm_source=reddit&utm_medium=sc&utm_campaign=springboot_spa_securing");
                links.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub",
                                "https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
                links.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)",
                                "https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
                links.put("Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub",
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

                // fetch owners from DB (ensures map isn't needed)
                User userOwner = userService.findByEmail(USER_EMAIL).orElse(null);
                User masterOwner = userService.findByEmail(MASTER_EMAIL).orElse(userOwner);
                if (userOwner == null && masterOwner == null) {
                        logger.warn("No users found to own links; seeding aborted.");
                        return;
                }

                final User u1 = userOwner != null ? userOwner : masterOwner;
                final User u2 = masterOwner != null ? masterOwner : u1;

                int created = 0;

                for (Map.Entry<String, String> e : links.entrySet()) {
                        String title = e.getKey();
                        String url = e.getValue();

                        User owner = (title.startsWith("Build")) ? u1 : u2;

                        Link link = new Link(title, url);
                        link.setUser(owner);
                        linkService.save(link);
                        created++;

                        Comment c1 = new Comment(
                                        "Thank you for this link related to Spring Boot. I love it, great post!", link);
                        Comment c2 = new Comment("I love that you're talking about Spring Security", link);
                        Comment c3 = new Comment(
                                        "What is this Progressive Web App thing all about? PWAs sound really cool.",
                                        link);

                        commentService.save(c1);
                        commentService.save(c2);
                        commentService.save(c3);

                        link.addComment(c1);
                        link.addComment(c2);
                        link.addComment(c3);
                }

                logger.info("Seeded {} links (total now: {})", created, linkService.count());
        }
}
