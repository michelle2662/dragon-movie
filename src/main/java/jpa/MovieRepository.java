package jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
