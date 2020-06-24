package be.pxl.VisitorsApplication.repository;

import be.pxl.VisitorsApplication.model.Department;
import be.pxl.VisitorsApplication.model.Patient;
import be.pxl.VisitorsApplication.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    boolean existsByPatient(Patient patient);

    @Query("SELECT v FROM Visitor v WHERE v.appointment = ?1 AND v.patient.department = ?2")
    List<Visitor> findAllByAppointmentAndDepartment(LocalTime time, Department department);

    @Query("SELECT v FROM Visitor v WHERE v.patient.department.code = ?1")
    List<Visitor> findAllByDepartmentCode(String departmentCode);

    @Query("SELECT v FROM Visitor v WHERE v.patient.department.code = ?1 ORDER BY v.appointment")
    List<Visitor> findAllByDepartmentCodeOrderByAppointment(String departmentCode);
}
