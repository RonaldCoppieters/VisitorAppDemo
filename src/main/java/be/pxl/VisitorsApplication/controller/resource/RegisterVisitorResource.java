package be.pxl.VisitorsApplication.controller.resource;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.Optional;

public class RegisterVisitorResource {
	@NotEmpty(message = "Patient code field must not be empty.")
	@Pattern(regexp = "^P\\d{3}$", message = "Patient code must be of format 'P###' (e.g. P001).")
	private String patientCode;
	@NotNull(message = "Appointment field must not be empty.")
	private LocalTime appointment;
	@NotEmpty(message = "Last name field must not be empty.")
	private String lastName;
	@NotEmpty(message = "First name field must not be empty.")
	private String firstName;
	@NotEmpty(message = "Phone number field must not be empty.")
	@Pattern(regexp = "^\\+\\d{1,2}-*\\d{6,24}$", message = "The phone number " +
			"must contain a country code identifier and only numbers (e.g. +12012984763).")
	private String phoneNumber;


	public String getPatientCode() {
		return patientCode;
	}

	public void setPatientCode(String patientCode) {
		this.patientCode = patientCode;
	}

	public LocalTime getAppointment() {
		return appointment;
	}

	public void setAppointment(LocalTime appointment) {
		this.appointment = appointment;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Optional<String> getPhoneNumber() {
		if (phoneNumber.isBlank()) return Optional.empty();
		else return Optional.of(phoneNumber);
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
