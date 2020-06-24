package be.pxl.VisitorsApplication.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Patient {
	@Id
	private String code;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Department department;

	private LocalDateTime admission;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getAdmission() {
		return admission;
	}

	public void setAdmission(LocalDateTime admission) {
		this.admission = admission;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}
