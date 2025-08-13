package com.adelahmadi.springit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.adelahmadi.springit.domain.Comment;
import com.adelahmadi.springit.domain.Link;
import com.adelahmadi.springit.repository.CommentRepository;
import com.adelahmadi.springit.repository.LinkRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @SpringBootApplication is a convenience annotation that adds all of the following:
// @Configuration, @EnableAutoConfiguration, and @ComponentScan with their default attributes.
// @EnableJpaAuditing is used to enable JPA auditing features, such as automatically setting created and modified timestamps on entities.
// This is useful for tracking when entities are created or modified in the database.
@SpringBootApplication
@EnableJpaAuditing
public class SpringitApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpringitApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringitApplication.class, args);
		logger.info("The Application \"Springit\" has started successfully");
	}

	// CommandLineRunner is a functional interface that can be used to run code
	// after the application context is loaded.
	// It is typically used to execute code that needs to run once the application
	// has started, such as initializing the database with sample data.
	// The method commandLineRunner will be executed after the application context
	// is loaded.
	// The parameters linkRepository and commentRepository are automatically
	// injected by Spring, allowing you to use them to interact with the database.
	// The @Bean annotation indicates that this method produces a bean to be managed
	// by the Spring container.
	// After performing the necessary tests in the previous section, @Bean is
	// commented out so that when the application is started, it does not create
	// sample data again.
	// If you want to run this code again, you can uncomment the @Bean annotation.
	// Uncommenting the @Bean annotation will allow the commandLineRunner method to
	// be executed when the application starts.
	// This is useful for initializing the database with sample data or performing
	// other startup tasks.
	// @Bean
	CommandLineRunner commandLineRunner(LinkRepository linkRepository, CommentRepository commentRepository) {
		return args -> {

			// Create a sample link
			/* Explane this code */
			// This code creates a new Link object with a title and URL, then saves it to
			// the database using the linkRepository.
			// It is typically used to initialize the database with some sample data when
			// the application starts.
			logger.info("Creating a sample link...");
			Link link = new Link();
			link.setTitle("Spring Boot 2.0 Released");
			link.setUrl("https://spring.io/blog/2018/03/01/spring-boot-2-0-0-released");
			linkRepository.save(link);

			// Create a sample comment
			/* Explane this code */
			// This code creates a new Comment object with a body and associates it with the
			logger.info("Creating a sample comment...");
			Comment comment = new Comment();
			comment.setBody("This is a great post about Spring Boot 2.0!");
			comment.setLink(link);
			commentRepository.save(comment);

			// Add the comment to the link
			// This code adds the comment to the link's list of comments, establishing a
			// relationship between the link and the comment.
			logger.info("Adding comment to the link...");
			link.addComment(comment);

			logger.info("Sample data created successfully");

			// Log the created link and comment
			logger.info("Link created: {}", link);
			logger.info("Comment created: {}", comment);

			/* Just for testing */
			// firt we need to have a query method in LinkRepository
			// LinkRepository.java
			// public interface LinkRepository extends JpaRepository<Link, Long> {
			// Link findByTitle(String title);
			// }
			// // Find the link by title
			// /* Explane this code */
			// // This code retrieves a Link object from the database by its title using the
			// // linkRepository.
			// logger.info("Finding link by title...");
			// Link foundLink = linkRepository.findByTitle("Spring Boot 2.0 Released");
			// if (foundLink != null) {
			// logger.info("Found link: {}", foundLink);
			// } else {
			// logger.warn("No link found with the specified title.");
			// }

		};
	}
}
