package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.Movie;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Showtime
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

//@Entity
public class Showtime {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("date_time")
	private OffsetDateTime dateTime = null;

	@JsonProperty("movie")
	private Movie movie = null;

	@JsonProperty("theater_box")
	private TheaterBox theaterBox = null;

	/**
	 * Unique identifier of the showtime.
	 * 
	 * @return id
	 **/
	@Schema(required = true, description = "Unique identifier of the showtime.")
	@NotNull
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Showtime dateTime(OffsetDateTime dateTime) {
		this.dateTime = dateTime;
		return this;
	}

	/**
	 * Date and time of the showtime.
	 * 
	 * @return dateTime
	 **/
	@Schema(required = true, description = "Date and time of the showtime.")
	@NotNull

	@Valid
	public OffsetDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(OffsetDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Showtime movie(Movie movie) {
		this.movie = movie;
		return this;
	}

	/**
	 * Get movie
	 * 
	 * @return movie
	 **/
	@Schema(required = true, description = "")
	@NotNull

	@Valid
	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Showtime theaterBox(TheaterBox theaterBox) {
		this.theaterBox = theaterBox;
		return this;
	}

	/**
	 * Get theaterBox
	 * 
	 * @return theaterBox
	 **/
	@Schema(required = true, description = "")
	@NotNull

	@Valid
	public TheaterBox getTheaterBox() {
		return theaterBox;
	}

	public void setTheaterBox(TheaterBox theaterBox) {
		this.theaterBox = theaterBox;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Showtime showtime = (Showtime) o;
		return Objects.equals(this.id, showtime.id) && Objects.equals(this.dateTime, showtime.dateTime)
				&& Objects.equals(this.movie, showtime.movie) && Objects.equals(this.theaterBox, showtime.theaterBox);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, dateTime, movie, theaterBox);
	}

	@Override
	public String toString() {
		return "Showtime [id=" + id + ", dateTime=" + dateTime + ", movie=" + movie + ", theaterBox=" + theaterBox
				+ "]";
	}

}
