package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.threeten.bp.LocalDate;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * MovieRequestBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T13:14:07.895681+01:00[Europe/London]")

public class MovieRequestBody {
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

	public MovieRequestBody title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Get title
	 * 
	 * @return title
	 **/
	@Schema(example = "Oppenheimer", description = "")
	@NotNull
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MovieRequestBody director(String director) {
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

	public MovieRequestBody genre(String genre) {
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

	public MovieRequestBody rating(String rating) {
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

	public MovieRequestBody length(String length) {
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

	public MovieRequestBody releaseDate(LocalDate releaseDate) {
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

	public MovieRequestBody reviewScore(BigDecimal reviewScore) {
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

	public MovieRequestBody currentlyPlaying(Boolean currentlyPlaying) {
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

	public MovieRequestBody upcomingRelease(Boolean upcomingRelease) {
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
		MovieRequestBody movieRequestBody = (MovieRequestBody) o;
		return Objects.equals(this.title, movieRequestBody.title)
				&& Objects.equals(this.director, movieRequestBody.director)
				&& Objects.equals(this.genre, movieRequestBody.genre)
				&& Objects.equals(this.rating, movieRequestBody.rating)
				&& Objects.equals(this.length, movieRequestBody.length)
				&& Objects.equals(this.releaseDate, movieRequestBody.releaseDate)
				&& Objects.equals(this.reviewScore, movieRequestBody.reviewScore)
				&& Objects.equals(this.currentlyPlaying, movieRequestBody.currentlyPlaying)
				&& Objects.equals(this.upcomingRelease, movieRequestBody.upcomingRelease);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, director, genre, rating, length, releaseDate, reviewScore, currentlyPlaying,
				upcomingRelease);
	}

	@Override
	public String toString() {
		return "MovieRequestBody [title=" + title + ", director=" + director + ", genre=" + genre + ", rating=" + rating
				+ ", length=" + length + ", releaseDate=" + releaseDate + ", reviewScore=" + reviewScore
				+ ", currentlyPlaying=" + currentlyPlaying + ", upcomingRelease=" + upcomingRelease + "]";
	}

}
