package be.pxl.VisitorsApplication.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Visitor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Patient patient;
	private String lastName;
	private String firstName;
	private String phoneNumber;
	private LocalTime appointment;
	private LocalDateTime entry;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getAppointment() {
		return appointment;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAppointment(LocalTime appointment) {
		this.appointment = appointment;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getInfo() {
		return lastName + " " + firstName + " (tel: " + phoneNumber + ")";
	}

	public LocalDateTime getEntry() {
		return entry;
	}

	public void setEntry(LocalDateTime entry) {
		this.entry = entry;
	}

	public boolean isReedsAangemeld(LocalDate datum) {
		return entry != null && entry.toLocalDate().equals(datum);
	}
}
