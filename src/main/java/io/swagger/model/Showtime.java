package io.swagger.model;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.v3.oas.annotations.media.Schema;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Showtime
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T15:29:40.414361-04:00[America/New_York]")

@Entity
public class Showtime {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id = null;

  @JsonProperty("date_time")
  private LocalDateTime dateTime = null;

  @ManyToOne
  @JoinColumn(name = "movie_id", referencedColumnName = "id")
  @JsonBackReference
  private Movie movie;

  @ManyToOne
  @JoinColumn(name = "theater_box_id", referencedColumnName = "id")
  @JsonBackReference
  private TheaterBox theaterBox = null;

  @OneToMany(mappedBy = "showtime")
  @JsonManagedReference
  private Set<Reservation> reservations;

  public Showtime() {

  }

  public Showtime(Long id, LocalDateTime dateTime, Movie movie, TheaterBox theaterBox) {
    this.id = id;
    this.dateTime = dateTime;
    this.movie = movie;
    this.theaterBox = theaterBox;
  }

  public Showtime id(Long id) {
    this.id = id;
    return this;
  }

    /**
   * Unique identifier of the showtime.
   * @return id
   **/
  @Schema(example = "104", required = true, description = "Unique identifier of the showtime.")
      @NotNull

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Showtime dateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  /**
   * Date and time of the showtime.
   * @return dateTime
   **/
  @Schema(example = "2023-12-15T20:00Z", required = true, description = "Date and time of the showtime.")
  @NotNull
  @Valid
  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }

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
   * ID of the theater box associated with the showtime.
   * @return theaterBoxId
   **/
  @Schema(example = "5", required = true, description = "ID of the theater box associated with the showtime.")
  @NotNull
  public TheaterBox getTheaterBox() {
    return theaterBox;
  }

  public void setTheaterBox(TheaterBox theaterBox) {
    this.theaterBox = theaterBox;
  }

  public Set<Reservation> getReservations() {
    return reservations;
  }

  public void setReservations(Set<Reservation> reservations) {
    this.reservations = reservations;
  }

  public Showtime reservations(Set<Reservation> reservations) {
    this.reservations = reservations;
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
    Showtime showtime = (Showtime) o;
    return Objects.equals(this.id, showtime.id) &&
        Objects.equals(this.dateTime, showtime.dateTime) &&
        Objects.equals(this.movie, showtime.movie) &&
        Objects.equals(this.theaterBox, showtime.theaterBox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateTime, movie, theaterBox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Showtime {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dateTime: ").append(toIndentedString(dateTime)).append("\n");
    sb.append("    movie: ").append(toIndentedString(movie)).append("\n");
    sb.append("    theaterBoxId: ").append(toIndentedString(theaterBox)).append("\n");
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
