package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReservationsBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-27T23:58:07.589943-04:00[America/New_York]")


public class ReservationsBody   {
  @JsonProperty("showtime_id")
  private Long showtimeId = null;

  @JsonProperty("seats_reserved")
  private Integer seatsReserved = null;

  public ReservationsBody showtimeId(Long showtimeId) {
    this.showtimeId = showtimeId;
    return this;
  }

  /**
   * Get showtimeId
   * @return showtimeId
   **/
  @Schema(description = "")
  
    public Long getShowtimeId() {
    return showtimeId;
  }

  public void setShowtimeId(Long showtimeId) {
    this.showtimeId = showtimeId;
  }

  public ReservationsBody seatsReserved(Integer seatsReserved) {
    this.seatsReserved = seatsReserved;
    return this;
  }

  /**
   * Get seatsReserved
   * @return seatsReserved
   **/
  @Schema(description = "")
  
    public Integer getSeatsReserved() {
    return seatsReserved;
  }

  public void setSeatsReserved(Integer seatsReserved) {
    this.seatsReserved = seatsReserved;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationsBody reservationsBody = (ReservationsBody) o;
    return Objects.equals(this.showtimeId, reservationsBody.showtimeId) &&
        Objects.equals(this.seatsReserved, reservationsBody.seatsReserved);
  }

  @Override
  public int hashCode() {
    return Objects.hash(showtimeId, seatsReserved);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReservationsBody {\n");
    
    sb.append("    showtimeId: ").append(toIndentedString(showtimeId)).append("\n");
    sb.append("    seatsReserved: ").append(toIndentedString(seatsReserved)).append("\n");
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
