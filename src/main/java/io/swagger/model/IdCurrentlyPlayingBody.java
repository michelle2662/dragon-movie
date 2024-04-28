package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * IdCurrentlyPlayingBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-27T18:43:33.268443-04:00[America/New_York]")


public class IdCurrentlyPlayingBody   {
  @JsonProperty("currentlyPlaying")
  private Boolean currentlyPlaying = null;

  public IdCurrentlyPlayingBody currentlyPlaying(Boolean currentlyPlaying) {
    this.currentlyPlaying = currentlyPlaying;
    return this;
  }

  /**
   * Get currentlyPlaying
   * @return currentlyPlaying
   **/
  @Schema(description = "")
  
    public Boolean isCurrentlyPlaying() {
    return currentlyPlaying;
  }

  public void setCurrentlyPlaying(Boolean currentlyPlaying) {
    this.currentlyPlaying = currentlyPlaying;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdCurrentlyPlayingBody idCurrentlyPlayingBody = (IdCurrentlyPlayingBody) o;
    return Objects.equals(this.currentlyPlaying, idCurrentlyPlayingBody.currentlyPlaying);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentlyPlaying);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdCurrentlyPlayingBody {\n");
    
    sb.append("    currentlyPlaying: ").append(toIndentedString(currentlyPlaying)).append("\n");
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
