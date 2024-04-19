package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Showtime
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T15:29:40.414361-04:00[America/New_York]")


public class Showtime   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("date_time")
  private OffsetDateTime dateTime = null;

  @JsonProperty("movie_id")
  private Integer movieId = null;

  @JsonProperty("theater_box_id")
  private Integer theaterBoxId = null;

  public Showtime id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of the showtime.
   * @return id
   **/
  @Schema(example = "104", required = true, description = "Unique identifier of the showtime.")
      @NotNull

    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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
  @Schema(example = "2023-12-15T20:00Z", required = true, description = "Date and time of the showtime.")
      @NotNull

    @Valid
    public OffsetDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public Showtime movieId(Integer movieId) {
    this.movieId = movieId;
    return this;
  }

  /**
   * ID of the movie associated with the showtime.
   * @return movieId
   **/
  @Schema(example = "123", required = true, description = "ID of the movie associated with the showtime.")
      @NotNull

    public Integer getMovieId() {
    return movieId;
  }

  public void setMovieId(Integer movieId) {
    this.movieId = movieId;
  }

  public Showtime theaterBoxId(Integer theaterBoxId) {
    this.theaterBoxId = theaterBoxId;
    return this;
  }

  /**
   * ID of the theater box associated with the showtime.
   * @return theaterBoxId
   **/
  @Schema(example = "5", required = true, description = "ID of the theater box associated with the showtime.")
      @NotNull

    public Integer getTheaterBoxId() {
    return theaterBoxId;
  }

  public void setTheaterBoxId(Integer theaterBoxId) {
    this.theaterBoxId = theaterBoxId;
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
        Objects.equals(this.movieId, showtime.movieId) &&
        Objects.equals(this.theaterBoxId, showtime.theaterBoxId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateTime, movieId, theaterBoxId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Showtime {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dateTime: ").append(toIndentedString(dateTime)).append("\n");
    sb.append("    movieId: ").append(toIndentedString(movieId)).append("\n");
    sb.append("    theaterBoxId: ").append(toIndentedString(theaterBoxId)).append("\n");
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