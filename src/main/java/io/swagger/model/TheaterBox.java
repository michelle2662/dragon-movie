package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TheaterBox
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")


public class TheaterBox   {
  @JsonProperty("box_number")
  private Integer boxNumber = null;

  @JsonProperty("total_seats")
  private Integer totalSeats = null;

  @JsonProperty("reserved_seats")
  private Integer reservedSeats = null;

  @JsonProperty("ticket_price")
  private Float ticketPrice = null;

  public TheaterBox boxNumber(Integer boxNumber) {
    this.boxNumber = boxNumber;
    return this;
  }

  /**
   * Number of the theater box.
   * @return boxNumber
   **/
  @Schema(description = "Number of the theater box.")
  
    public Integer getBoxNumber() {
    return boxNumber;
  }

  public void setBoxNumber(Integer boxNumber) {
    this.boxNumber = boxNumber;
  }

  public TheaterBox totalSeats(Integer totalSeats) {
    this.totalSeats = totalSeats;
    return this;
  }

  /**
   * Total number of seats in the theater box.
   * @return totalSeats
   **/
  @Schema(description = "Total number of seats in the theater box.")
  
    public Integer getTotalSeats() {
    return totalSeats;
  }

  public void setTotalSeats(Integer totalSeats) {
    this.totalSeats = totalSeats;
  }

  public TheaterBox reservedSeats(Integer reservedSeats) {
    this.reservedSeats = reservedSeats;
    return this;
  }

  /**
   * Number of reserved seats in the theater box.
   * @return reservedSeats
   **/
  @Schema(description = "Number of reserved seats in the theater box.")
  
    public Integer getReservedSeats() {
    return reservedSeats;
  }

  public void setReservedSeats(Integer reservedSeats) {
    this.reservedSeats = reservedSeats;
  }

  public TheaterBox ticketPrice(Float ticketPrice) {
    this.ticketPrice = ticketPrice;
    return this;
  }

  /**
   * Price of a ticket for the theater box.
   * @return ticketPrice
   **/
  @Schema(description = "Price of a ticket for the theater box.")
  
    public Float getTicketPrice() {
    return ticketPrice;
  }

  public void setTicketPrice(Float ticketPrice) {
    this.ticketPrice = ticketPrice;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TheaterBox theaterBox = (TheaterBox) o;
    return Objects.equals(this.boxNumber, theaterBox.boxNumber) &&
        Objects.equals(this.totalSeats, theaterBox.totalSeats) &&
        Objects.equals(this.reservedSeats, theaterBox.reservedSeats) &&
        Objects.equals(this.ticketPrice, theaterBox.ticketPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boxNumber, totalSeats, reservedSeats, ticketPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TheaterBox {\n");
    
    sb.append("    boxNumber: ").append(toIndentedString(boxNumber)).append("\n");
    sb.append("    totalSeats: ").append(toIndentedString(totalSeats)).append("\n");
    sb.append("    reservedSeats: ").append(toIndentedString(reservedSeats)).append("\n");
    sb.append("    ticketPrice: ").append(toIndentedString(ticketPrice)).append("\n");
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
