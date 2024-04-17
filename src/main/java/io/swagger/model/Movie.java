package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.validation.Valid;

/**
 * Movie
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Movie {
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

	public Movie title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Get title
	 * 
	 * @return title
	 **/
	@Schema(example = "Oppenheimer", description = "")

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Movie director(String director) {
		this.director = director;
		return this;
	}

	/**
	 * Get director
	 * 
	 * @return director
	 **/
	@Schema(example = "Christopher Nolan", description = "")
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public Movie genre(String genre) {
		this.genre = genre;
		return this;
	}

	/**
	 * Get genre
	 * 
	 * @return genre
	 **/
	@Schema(example = "Thriller", description = "")
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Movie rating(String rating) {
		this.rating = rating;
		return this;
	}

	/**
	 * Get rating
	 * 
	 * @return rating
	 **/
	@Schema(example = "R", description = "")
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public Movie length(String length) {
		this.length = length;
		return this;
	}

	/**
	 * Get length
	 * 
	 * @return length
	 **/
	@Schema(example = "3h5m", description = "")
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public Movie releaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
		return this;
	}

	/**
	 * Get releaseDate
	 * 
	 * @return releaseDate
	 **/
	@Schema(example = "Fri Jul 21 01:00:00 BST 2023", description = "")
	@Valid
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Movie reviewScore(BigDecimal reviewScore) {
		this.reviewScore = reviewScore;
		return this;
	}

	/**
	 * Get reviewScore
	 * 
	 * @return reviewScore
	 **/
	@Schema(example = "8.9", description = "")
	@Valid
	public BigDecimal getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(BigDecimal reviewScore) {
		this.reviewScore = reviewScore;
	}

	public Movie currentlyPlaying(Boolean currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
		return this;
	}

	/**
	 * Get currentlyPlaying
	 * 
	 * @return currentlyPlaying
	 **/
	@Schema(example = "true", description = "")
	public Boolean isCurrentlyPlaying() {
		return currentlyPlaying;
	}

	public void setCurrentlyPlaying(Boolean currentlyPlaying) {
		this.currentlyPlaying = currentlyPlaying;
	}

	public Movie upcomingRelease(Boolean upcomingRelease) {
		this.upcomingRelease = upcomingRelease;
		return this;
	}

	/**
	 * Get upcomingRelease
	 * 
	 * @return upcomingRelease
	 **/
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
		StringBuilder sb = new StringBuilder();
		sb.append("class Movie {\n");

		sb.append("    title: ").append(toIndentedString(title)).append("\n");
		sb.append("    director: ").append(toIndentedString(director)).append("\n");
		sb.append("    genre: ").append(toIndentedString(genre)).append("\n");
		sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
		sb.append("    length: ").append(toIndentedString(length)).append("\n");
		sb.append("    releaseDate: ").append(toIndentedString(releaseDate)).append("\n");
		sb.append("    reviewScore: ").append(toIndentedString(reviewScore)).append("\n");
		sb.append("    currentlyPlaying: ").append(toIndentedString(currentlyPlaying)).append("\n");
		sb.append("    upcomingRelease: ").append(toIndentedString(upcomingRelease)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
