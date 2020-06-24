package be.pxl.VisitorsApplication;

import be.pxl.VisitorsApplication.builder.*;
import be.pxl.VisitorsApplication.controller.resource.RegisterVisitorResource;
import be.pxl.VisitorsApplication.model.Department;
import be.pxl.VisitorsApplication.model.Patient;
import be.pxl.VisitorsApplication.model.Visitor;
import be.pxl.VisitorsApplication.repository.PatientRepository;
import be.pxl.VisitorsApplication.repository.VisitorRepository;
import be.pxl.VisitorsApplication.service.VisitorService;
import be.pxl.VisitorsApplication.util.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitorServiceTest {

    VisitorRepository visitorRepository;
    PatientRepository patientRepository;

    HttpServletRequest request;

    VisitorService visitorService;

    @BeforeEach
    public void setup() {
        visitorRepository = mock(VisitorRepository.class);
        patientRepository = mock(PatientRepository.class);
        request = mock(HttpServletRequest.class);

        visitorService = new VisitorService(visitorRepository, patientRepository, request);
    }

    @Test
    public void register_SavesValidRequest() {
        Visitor visitor = VisitorBuilder.BuildValidAndComplete();
        RegisterVisitorResource resource = RegisterVisitorConverter.Convert(visitor);

        when(patientRepository
                .findById(resource.getPatientCode()))
                .thenReturn(Optional.of(visitor.getPatient()));
        when(visitorRepository.existsByPatient(visitor.getPatient())).thenReturn(false);
        when(visitorRepository
                .findAllByAppointmentAndDepartment(resource.getAppointment(), visitor.getPatient().getDepartment()))
                .thenReturn(new ArrayList<>());

        boolean exceptionWasThrown = false;

        try {
            visitorService.register(resource);
        } catch (BadRequestException e) {
            exceptionWasThrown = true;
        }

        assertFalse(exceptionWasThrown);

        verify(visitorRepository).save(any(Visitor.class));
    }

    @Test
    public void register_ThrowsBadRequestExceptionWhenAppointmentIsNotWithinExpectedTimeframe() {
        VisitorBuilder visitorBuilder = new VisitorBuilder();
        Visitor earlyVisitor = visitorBuilder.withAppointment(ScheduleState.BEFORE).build();
        Visitor lateVisitor = visitorBuilder.withAppointment(ScheduleState.AFTER).build();

        RegisterVisitorResource earlyRegistration = RegisterVisitorConverter.Convert(earlyVisitor);
        RegisterVisitorResource lateRegistration = RegisterVisitorConverter.Convert(lateVisitor);

        assertThrows(BadRequestException.class, () -> visitorService.register(earlyRegistration));
        assertThrows(BadRequestException.class, () -> visitorService.register(lateRegistration));

        verifyNoInteractions(visitorRepository);
    }

    @Test
    public void register_ThrowsBadRequestExceptionWhenDepartmentTimeSlotAtCapacity() {
        Department department = new DepartmentBuilder().buildRandom();
        List<Visitor> visitors = new ArrayList<>();

        for (int i = 0; i < VisitorService.VISITORS_ALLOWED_PER_DEPARTMENT_PER_TIMEFRAME; i++) {
            Visitor visitor = VisitorBuilder.BuildValidAndComplete();
            visitor.getPatient().setDepartment(department);
            visitors.add(visitor);
        }

        Visitor visitor = VisitorBuilder.BuildValidAndComplete();
        visitor.getPatient().setDepartment(department);
        RegisterVisitorResource resource = RegisterVisitorConverter.Convert(visitor);

        when(patientRepository
                .findById(resource.getPatientCode()))
                .thenReturn(Optional.of(visitor.getPatient()));
        when(visitorRepository.existsByPatient(visitor.getPatient())).thenReturn(false);
        when(visitorRepository
                .findAllByAppointmentAndDepartment(resource.getAppointment(), department))
                .thenReturn(visitors);

        assertThrows(BadRequestException.class, () -> visitorService.register(resource));

        verify(patientRepository).findById(resource.getPatientCode());
        verify(visitorRepository).existsByPatient(visitor.getPatient());
        verify(visitorRepository).findAllByAppointmentAndDepartment(resource.getAppointment(), department);
        verifyNoMoreInteractions(visitorRepository, patientRepository);
    }

    @Test
    public void register_ThrowsBadRequestExceptionWhenPatientAlreadyHasVisitor() {
        Visitor visitor = VisitorBuilder.BuildValidAndComplete();
        Patient patient = new PatientBuilder().withDepartment().withAdmission().build();
        RegisterVisitorResource resource = RegisterVisitorConverter.Convert(visitor);
        patient.setCode(resource.getPatientCode());

        when(patientRepository.findById(resource.getPatientCode()))
                .thenReturn(Optional.of(patient));
        when(visitorRepository.existsByPatient(patient)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> visitorService.register(resource));

        verify(patientRepository).findById(resource.getPatientCode());
        verify(visitorRepository).existsByPatient(patient);
        verifyNoMoreInteractions(patientRepository, visitorRepository);
    }

    @Test
    public void register_ThrowsBadRequestExceptionWhenPatientDoesNotExist() {
        Visitor visitor = VisitorBuilder.BuildValidAndComplete();
        RegisterVisitorResource resource = RegisterVisitorConverter.Convert(visitor);

        when(patientRepository.findById(resource.getPatientCode())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> visitorService.register(resource));

        verify(patientRepository).findById(resource.getPatientCode());
        verifyNoMoreInteractions(patientRepository);

    }

    @Test void validateEntry_SavesValidVisitor() {
        VisitorBuilder visitorBuilder = new VisitorBuilder();
        Visitor returnedVisitor = visitorBuilder
                .withId()
                .withAppointment(ScheduleState.VALID)
                .build();
        Visitor visitor = visitorBuilder
                .withEntry(ScheduleState.VALID)
                .build();

        when(visitorRepository.findById(visitor.getId()))
                .thenReturn(Optional.of(returnedVisitor));

        boolean exceptionWasThrown = false;

        try {
            visitorService.validateEntryAndSave(visitor.getId(), visitor.getEntry());
        } catch (BadRequestException e) {
            exceptionWasThrown = true;
        }

        assertFalse(exceptionWasThrown);

        verify(visitorRepository).findById(visitor.getId());
        verify(visitorRepository).save(returnedVisitor);
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    public void validateEntry_ThrowsBadRequestExceptionWhenEntryTimeIsNull() {
        Visitor visitor = new VisitorBuilder().withId().build();

        assertThrows(BadRequestException.class, () -> visitorService
                .validateEntryAndSave(visitor.getId(), null));

        verifyNoInteractions(visitorRepository);
    }


    @Test
    public void validateEntry_ThrowsBadRequestExceptionWhenVisitorAlreadyVisitedThatDay() {
        Visitor visitor = new VisitorBuilder()
                .withId()
                .withAppointment(ScheduleState.VALID)
                .withEntry(ScheduleState.VALID)
                .build();

        when(visitorRepository.findById(visitor.getId()))
                .thenReturn(Optional.of(visitor));

        assertThrows(BadRequestException.class, () -> visitorService
                .validateEntryAndSave(visitor.getId(), visitor.getEntry()));

        verify(visitorRepository).findById(visitor.getId());
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    public void validateEntry_ThrowsBadRequestExceptionWhenVisitorDoesNotExist() {
        Visitor visitor = new VisitorBuilder()
                .withId()
                .withAppointment(ScheduleState.VALID)
                .withEntry(ScheduleState.VALID)
                .build();

        when(visitorRepository.findById(visitor.getId()))
                .thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> visitorService
                .validateEntryAndSave(visitor.getId(), visitor.getEntry()));

        verify(visitorRepository).findById(visitor.getId());
        verifyNoMoreInteractions(visitorRepository);
    }

    @Test
    public void validateEntry_ThrowsBadRequestExceptionWhenVisitorIsNotOnTime() {
        VisitorBuilder visitorBuilder = new VisitorBuilder()
                .withId()
                .withAppointment(ScheduleState.VALID);

        Visitor visitor = visitorBuilder.build();
        Visitor earlyVisitor = visitorBuilder.withEntry(ScheduleState.BEFORE).build();
        Visitor lateVisitor = visitorBuilder.withEntry(ScheduleState.AFTER).build();

        when(visitorRepository.findById(visitor.getId()))
                .thenReturn(Optional.of(visitor));

        assertThrows(BadRequestException.class,
                () -> visitorService.validateEntryAndSave(visitor.getId(), earlyVisitor.getEntry()));
        assertThrows(BadRequestException.class,
                () -> visitorService.validateEntryAndSave(visitor.getId(), lateVisitor.getEntry()));

        verify(visitorRepository, times(2)).findById(visitor.getId());
        verifyNoMoreInteractions(visitorRepository);
    }

}
