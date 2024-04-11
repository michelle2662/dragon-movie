package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.TheaterBox;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Reservation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class Reservation   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("showtime_id")
  private AllOfReservationShowtimeId showtimeId = null;

  @JsonProperty("theater_box")
  private TheaterBox theaterBox = null;

  public Reservation id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Identifier for reservation
   * @return id
   **/
  @Schema(description = "Identifier for reservation")
  
    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Reservation showtimeId(AllOfReservationShowtimeId showtimeId) {
    this.showtimeId = showtimeId;
    return this;
  }

  /**
   * Identifier of the showtime associated with the reservation
   * @return showtimeId
   **/
  @Schema(description = "Identifier of the showtime associated with the reservation")
  
    public AllOfReservationShowtimeId getShowtimeId() {
    return showtimeId;
  }

  public void setShowtimeId(AllOfReservationShowtimeId showtimeId) {
    this.showtimeId = showtimeId;
  }

  public Reservation theaterBox(TheaterBox theaterBox) {
    this.theaterBox = theaterBox;
    return this;
  }

  /**
   * Get theaterBox
   * @return theaterBox
   **/
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
    return Objects.equals(this.id, reservation.id) &&
        Objects.equals(this.showtimeId, reservation.showtimeId) &&
        Objects.equals(this.theaterBox, reservation.theaterBox);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, showtimeId, theaterBox);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Reservation {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    showtimeId: ").append(toIndentedString(showtimeId)).append("\n");
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
