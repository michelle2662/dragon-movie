package io.swagger.configuration;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.threeten.bp.LocalDate;

import io.swagger.jpa.MembershipRepository;
import io.swagger.jpa.MovieRepository;
import io.swagger.model.Membership;
import io.swagger.model.Movie;


@Configuration
public class DatabaseInitialization {
	
	private static final Logger log = LoggerFactory.getLogger(DatabaseInitialization.class);

	@Bean
	public CommandLineRunner initDatabase(MovieRepository movieRepository, MembershipRepository membershipRepository) {
		log.info("Preloading data");
		
		return args -> {
			// id = 1
			movieRepository.save(new Movie("Dune: Part Two", "Denis Villeneuve", "Action", "PG-13", "2h46m",
					LocalDate.parse("2024-02-06"), new BigDecimal(8.7), true, false));
			
			// id = 1
			membershipRepository.save(new Membership("Jane", "Doe", "jane.doe@email.com"));
		};
	}

}
