package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Reservation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

//@Entity
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("showtime_id")
	private Long showtimeId = null;
	
	@JsonProperty("theater_box")
	private TheaterBox theaterBox = null;

	@Schema(description = "Identifier for reservation")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Reservation showtimeId(Long showtimeId) {
		this.showtimeId = showtimeId;
		return this;
	}

	public Long getShowtimeId() {
		return showtimeId;
	}

	public void setShowtimeId(Long showtimeId) {
		this.showtimeId = showtimeId;
	}

	public Reservation theaterBox(TheaterBox theaterBox) {
		this.theaterBox = theaterBox;
		return this;
	}

	@Schema(description = "")
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
		Reservation reservation = (Reservation) o;
		return Objects.equals(this.id, reservation.id) && Objects.equals(this.showtimeId, reservation.showtimeId)
				&& Objects.equals(this.theaterBox, reservation.theaterBox);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, showtimeId, theaterBox);
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", showtimeId=" + showtimeId + ", theaterBoxNumber=" + theaterBox + "]";
	}

}
