package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.Movie;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Showtime
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class Showtime   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("date_time")
  private OffsetDateTime dateTime = null;

  @JsonProperty("movie")
  private Movie movie = null;

  @JsonProperty("theater_box")
  private TheaterBox theaterBox = null;

  public Showtime id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of the showtime.
   * @return id
   **/
  @Schema(required = true, description = "Unique identifier of the showtime.")
      @NotNull

    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Showtime dateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  /**
   * Date and time of the showtime.
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
    sb.append("    theaterBox: ").append(toIndentedString(theaterBox)).append("\n");
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
