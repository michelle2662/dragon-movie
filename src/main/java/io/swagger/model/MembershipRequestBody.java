package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.*;

/**
 * MembershipRequestBody
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T13:14:07.895681+01:00[Europe/London]")

public class MembershipRequestBody {
	@JsonProperty("firstName")
	private String firstName = null;

	@JsonProperty("lastName")
	private String lastName = null;

	@JsonProperty("email")
	private String email = null;

	public MembershipRequestBody firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * Get firstName
	 * 
	 * @return firstName
	 **/
	@Schema(example = "John", required = true, description = "")
	@NotNull
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public MembershipRequestBody lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * Get lastName
	 * 
	 * @return lastName
	 **/
	@Schema(example = "Smith", required = true, description = "")
	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public MembershipRequestBody email(String email) {
		this.email = email;
		return this;
	}

	/**
	 * Get email
	 * 
	 * @return email
	 **/
	@Schema(example = "john.smith@email.com", required = true, description = "")
	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MembershipRequestBody membershipRequestBody = (MembershipRequestBody) o;
		return Objects.equals(this.firstName, membershipRequestBody.firstName)
				&& Objects.equals(this.lastName, membershipRequestBody.lastName)
				&& Objects.equals(this.email, membershipRequestBody.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstName, lastName, email);
	}

	@Override
	public String toString() {
		return "MembershipRequestBody [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

}
