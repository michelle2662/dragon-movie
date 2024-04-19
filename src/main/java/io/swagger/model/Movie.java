package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Movie
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Movie {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@JsonProperty("title")
	private String title = null;

	@JsonProperty("director")
	private String director = null;

	@JsonProperty("genre")
	private String genre = null;

	@JsonProperty("rating")
	private String rating = null;

	@JsonProperty("length")
	private String length = null;

	@JsonProperty("releaseDate")
	private LocalDate releaseDate = null;

	@JsonProperty("reviewScore")
	private BigDecimal reviewScore = null;

	@JsonProperty("currentlyPlaying")
	private Boolean currentlyPlaying = null;

	@JsonProperty("upcomingRelease")
	private Boolean upcomingRelease = null;

	public Movie() {
		
	}

	public Movie(Long id, String title, String director, String genre, String rating, String length, LocalDate releaseDate,
			BigDecimal reviewScore, Boolean currentlyPlaying, Boolean upcomingRelease) {
		super();
		this.id = id;
		this.title = title;
		this.director = director;
		this.genre = genre;
		this.rating = rating;
		this.length = length;
		this.releaseDate = releaseDate;
		this.reviewScore = reviewScore;
		this.currentlyPlaying = currentlyPlaying;
		this.upcomingRelease = upcomingRelease;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Schema(example = "Oppenheimer", description = "")
	@NotNull
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Schema(example = "Christopher Nolan", description = "")
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	@Schema(example = "Thriller", description = "")
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Schema(example = "R", description = "")
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Schema(example = "3h5m", description = "")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@Schema(example = "Fri Jul 21 01:00:00 BST 2023", description = "")
	@Valid
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Schema(example = "8.9", description = "")
	@Valid
	@Min(0)
	@Max(10)
	public BigDecimal getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(BigDecimal reviewScore) {
		this.reviewScore = reviewScore;
	}

	@Schema(example = "true", description = "")
	public Boolean isCurrentlyPlaying() {
		return currentlyPlaying;
	}

	public void setCurrentlyPlaying(Boolean currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
	}

	@Schema(example = "false", description = "")
	public Boolean isUpcomingRelease() {
		return upcomingRelease;
	}

	public void setUpcomingRelease(Boolean upcomingRelease) {
		this.upcomingRelease = upcomingRelease;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Movie movie = (Movie) o;
		return Objects.equals(this.title, movie.title) && Objects.equals(this.director, movie.director)
				&& Objects.equals(this.genre, movie.genre) && Objects.equals(this.rating, movie.rating)
				&& Objects.equals(this.length, movie.length) && Objects.equals(this.releaseDate, movie.releaseDate)
				&& Objects.equals(this.reviewScore, movie.reviewScore)
				&& Objects.equals(this.currentlyPlaying, movie.currentlyPlaying)
				&& Objects.equals(this.upcomingRelease, movie.upcomingRelease);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, director, genre, rating, length, releaseDate, reviewScore, currentlyPlaying,
				upcomingRelease);
	}

	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", director=" + director + ", genre=" + genre + ", rating="
				+ rating + ", length=" + length + ", releaseDate=" + releaseDate + ", reviewScore=" + reviewScore
				+ ", currentlyPlaying=" + currentlyPlaying + ", upcomingRelease=" + upcomingRelease + "]";
	}

}
