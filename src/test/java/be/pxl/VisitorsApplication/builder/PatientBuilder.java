package be.pxl.VisitorsApplication.builder;

import be.pxl.VisitorsApplication.model.Patient;

import java.time.LocalDateTime;
import java.util.Random;

public class PatientBuilder {
    Random random;
    Patient patient;

    public PatientBuilder() {
        this.random = new Random();
        this.patient = new Patient();
    }

    public Patient build() {
        Patient patient = new Patient();
        patient.setAdmission(this.patient.getAdmission());
        patient.setDepartment(this.patient.getDepartment());
        patient.setCode(this.patient.getCode());
        return patient;
    }

    public PatientBuilder withAdmission() {
        int days = random.nextInt(361);
        this.patient.setAdmission(LocalDateTime.now().minusDays(days));
        return this;
    }

    public PatientBuilder withCode() {
        int id = random.nextInt(1000 + 100) - 100;
        this.patient.setCode(String.format("P%d", id));
        return this;
    }

    public PatientBuilder withDepartment() {
        this.patient.setDepartment(new DepartmentBuilder().buildRandom());
        return this;
    }
}
