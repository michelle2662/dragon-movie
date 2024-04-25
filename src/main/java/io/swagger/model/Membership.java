package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;


/**
 * Membership
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")

@Entity
public class Membership {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@JsonProperty("firstName")
	private String firstName = null;

	@JsonProperty("lastName")
	private String lastName = null;

	@JsonProperty("email")
	@Column(unique = true)
	private String email = null;

	@JsonProperty("password")
	private String password;

	@JsonProperty("role")
    private String role;

	public Membership() {

	}

	public Membership(Long id, String firstName, String lastName, String email, String password, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Schema(example = "John", description = "")
	@NotNull
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Schema(example = "Smith", description = "")
	@NotNull
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Schema(example = "john.smith@email.com", description = "")
	@NotNull
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Schema(example = "password", description = "")
	@NotNull
	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Schema(example = "MEMBER", description = "")
	@NotNull
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Membership membership = (Membership) o;
		return Objects.equals(this.firstName, membership.firstName)
				&& Objects.equals(this.lastName, membership.lastName) && Objects.equals(this.email, membership.email) && Objects.equals(this.password, membership.password) &&
                Objects.equals(this.role, membership.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, email, password, role);
		}

	@Override
	public String toString() {
		return "Membership [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", role='" + role 
				+ "]";
	}


}
