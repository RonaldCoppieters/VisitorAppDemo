package be.pxl.VisitorsApplication.service;

import be.pxl.VisitorsApplication.controller.resource.BadRequestCodes;
import be.pxl.VisitorsApplication.model.Department;
import be.pxl.VisitorsApplication.model.Patient;
import be.pxl.VisitorsApplication.model.Visitor;
import be.pxl.VisitorsApplication.controller.resource.RegisterVisitorResource;
import be.pxl.VisitorsApplication.repository.PatientRepository;
import be.pxl.VisitorsApplication.repository.VisitorRepository;
import be.pxl.VisitorsApplication.util.VisitorScheduleUtil;
import be.pxl.VisitorsApplication.util.exception.BadRequestException;
import be.pxl.VisitorsApplication.util.exception.InvalidSheduleTimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class VisitorService {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Logger LOGGER = LogManager.getLogger(VisitorService.class);
    public static final int VISITORS_ALLOWED_PER_DEPARTMENT_PER_TIMEFRAME = 2;

    private final VisitorRepository visitorRepository;
    private final PatientRepository patientRepository;
    private final HttpServletRequest request;

    public VisitorService(VisitorRepository visitorRepository,
                          PatientRepository patientRepository,
                          HttpServletRequest request) {
        this.visitorRepository = visitorRepository;
        this.patientRepository = patientRepository;
        this.request = request;
    }

    public List<Visitor> getVisitorsForDepartment(String departmentCode) {
        return visitorRepository.findAllByDepartmentCodeOrderByAppointment(departmentCode);
    }

    public Long register(RegisterVisitorResource resource) throws BadRequestException {
        ensureValidAppointment(resource.getAppointment());

        Patient patient = getPatient(resource.getPatientCode());

        ensurePatientDoesNotAlreadyHaveVisitor(patient);

        ensureDepartmentCanFitAppointment(resource.getAppointment(), patient.getDepartment());

        Visitor visitor = new Visitor();

        visitor.setLastName(resource.getLastName());
        visitor.setFirstName(resource.getFirstName());
        visitor.setPhoneNumber(resource.getPhoneNumber().orElse(""));
        visitor.setAppointment(resource.getAppointment());
        visitor.setPatient(patient);

        visitorRepository.save(visitor);

        return visitor.getId();
    }

    public void validateEntryAndSave(Long visitorId, LocalDateTime entryTime) throws BadRequestException {
        ensureValidEntryTime(entryTime);

        Visitor visitor = getVisitor(visitorId);

        ensureValidEntry(entryTime, visitor.getAppointment());

        if (visitor.getEntry() != null)
            ensureOneVisitPerDayPerPatient(visitor.getEntry(), entryTime);

        visitor.setEntry(entryTime);

        visitorRepository.save(visitor);
    }

    private void ensureDepartmentCanFitAppointment(LocalTime appointment, Department department) throws BadRequestException {
        if (visitorRepository.findAllByAppointmentAndDepartment(
                appointment,
                department)
                .size() >= VISITORS_ALLOWED_PER_DEPARTMENT_PER_TIMEFRAME) {
            throw new BadRequestException(
                    BadRequestCodes.SCHEDULE_CONSTRAINT_VIOLATION.name(),
                    String.format("No more reservations can be made at '%s'. Please pick another time.",
                            appointment.format(DateTimeFormatter.ofPattern("HH:mm"))),
                    request.getContextPath() + request.getServletPath());
        }
    }

    private void ensurePatientDoesNotAlreadyHaveVisitor(Patient patient) throws BadRequestException {
        if (visitorRepository.existsByPatient(patient)) {
            throw new BadRequestException(
                    BadRequestCodes.LIMIT_REACHED.name(),
                    "This patient cannot take on more visitors.",
                    request.getContextPath() + request.getServletPath());
        }
    }

    private void ensureValidAppointment(LocalTime appointment) throws BadRequestException {
        try {
            VisitorScheduleUtil.validateVisitationMoment(appointment);
        } catch (InvalidSheduleTimeException e) {
            throw new BadRequestException(
                    BadRequestCodes.SCHEDULE_CONSTRAINT_VIOLATION.name(),
                    e.getMessage(),
                    request.getContextPath() + request.getServletPath());
        }
    }

    private void ensureValidEntry(LocalDateTime entryTime, LocalTime appointment) throws BadRequestException {
        try {
            VisitorScheduleUtil.validateEntryMoment(entryTime, appointment);
        } catch (InvalidSheduleTimeException e) {
            throw new BadRequestException(
                    BadRequestCodes.SCHEDULE_CONSTRAINT_VIOLATION.name(),
                    e.getMessage(),
                    request.getContextPath() + request.getServletPath());
        }
    }

    private void ensureValidEntryTime(LocalDateTime entryTime) throws BadRequestException {
        if (entryTime == null)
            throw new BadRequestException(
                    BadRequestCodes.WRONG_REQUEST_FORMAT.name(),
                    "Entry time must be of format 'yyyyMMddHH:mm'.",
                    request.getContextPath() + request.getServletPath());
    }

    private void ensureOneVisitPerDayPerPatient(LocalDateTime oldValue, LocalDateTime newValue) throws BadRequestException {
        if (oldValue
                .format(DATE_FORMAT)
                .equals(newValue.format(DATE_FORMAT)))
            throw new BadRequestException(
                    BadRequestCodes.LIMIT_REACHED.name(),
                    "A visitor cannot visit more than once on the same day.",
                    request.getContextPath() + request.getServletPath());
    }

    private Patient getPatient(String patientCode) throws BadRequestException {
        Optional<Patient> patientOptional = patientRepository.findById(patientCode);

        if (patientOptional.isEmpty()) {
            throw new BadRequestException(
                    BadRequestCodes.DOES_NOT_EXIST.name(),
                    String.format("No patient with code '%s' exists.", patientCode),
                    request.getContextPath() + request.getServletPath());
        }

        return patientOptional.get();
    }

    private Visitor getVisitor(Long visitorId) throws BadRequestException {
        Optional<Visitor> visitorOptional = visitorRepository.findById(visitorId);

        if (visitorOptional.isEmpty())
            throw new BadRequestException(
                    BadRequestCodes.DOES_NOT_EXIST.name(),
                    String.format("No visitor with id '%d' exists.", visitorId),
                    request.getContextPath() + request.getServletPath());

        return visitorOptional.get();
    }

}
