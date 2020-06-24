package be.pxl.VisitorsApplication.repository;

import be.pxl.VisitorsApplication.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {
}
