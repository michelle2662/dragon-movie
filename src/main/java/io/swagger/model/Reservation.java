package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Reservation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Reservation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private Long id = null;

	@ManyToOne
	@JsonBackReference
    @JoinColumn(name = "showtime_id", referencedColumnName = "id")
	private Showtime showtime = null;
	
	@ManyToOne
	@JsonBackReference
    @JoinColumn(name = "theater_box_id", referencedColumnName = "id")
    private TheaterBox theaterBox = null;

	@JsonProperty("seatsReserved")
	private Integer seatsReserved = null;

	public Reservation() {

	}

	public Reservation(Long id, Showtime showtime, TheaterBox theaterBox, Integer seatsReserved) {
		this.id = id;
		this.showtime = showtime;
		this.theaterBox = theaterBox;
		this.seatsReserved = seatsReserved;
	}

	@Schema(description = "Identifier for reservation")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Reservation showtimeId(Showtime showtime) {
		this.showtime = showtime;
		return this;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
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

	@Schema(description = "Number of seats reserved")
	public Integer getSeatsReserved() {
		return seatsReserved;
	}

	public void setSeatsReserved(Integer seatsReserved) {
		this.seatsReserved = seatsReserved;
	}

	public Reservation seatsReserved(Integer seatsReserved) {
		this.seatsReserved = seatsReserved;
		return this;
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
		return Objects.equals(this.id, reservation.id) && Objects.equals(this.showtime, reservation.showtime)
				&& Objects.equals(this.theaterBox, reservation.theaterBox) && Objects.equals(this.seatsReserved, reservation.seatsReserved);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, showtime, theaterBox, seatsReserved);
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", showtime=" + showtime+ ", theaterBoxNumber=" + theaterBox + ", seatsReserved=" + seatsReserved + "]";
	}

}
