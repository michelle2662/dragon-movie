package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReservationModifyBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class ReservationModifyBody   {
  @JsonProperty("seats")
  private Integer seats = null;

  public ReservationModifyBody seats(Integer seats) {
    this.seats = seats;
    return this;
  }

  /**
   * New number of seats for the reservation.
   * minimum: 1
   * @return seats
   **/
  @Schema(required = true, description = "New number of seats for the reservation.")
      @NotNull

  @Min(1)  public Integer getSeats() {
    return seats;
  }

  public void setSeats(Integer seats) {
    this.seats = seats;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationModifyBody reservationModifyBody = (ReservationModifyBody) o;
    return Objects.equals(this.seats, reservationModifyBody.seats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReservationModifyBody {\n");
    
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
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
