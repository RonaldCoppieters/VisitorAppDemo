package be.pxl.VisitorsApplication.builder;

import be.pxl.VisitorsApplication.model.Visitor;
import be.pxl.VisitorsApplication.util.VisitorScheduleUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class VisitorBuilder {

    private static final String[] FIRST_NAMES = new String[]{
            "Nancy",
            "Benedict",
            "Nancie",
            "Corban",
            "Ritchie",
            "Ema",
            "Findlay",
            "Renesmee",
            "Keon",
            "Toyah"
    };

    private static final String[] LAST_NAMES = new String[]{
            "O'Doherty",
            "Van Lederhose",
            "Neale",
            "Gale",
            "Shaffer",
            "Weber",
            "Flower",
            "Mata",
            "Forrest",
            "Espinosa",
    };

    private static final String[] PHONE_NUMBERS = new String[]{
            "+12025550166",
            "+93497129844",
            "+355499127366",
            "+1-684476594141",
            "+502470134912",
            "+33485229316",
            "+32479266659",
            "+44-1624489149265",
            "+44-1534478715489",
            "+228471210525",
    };

    Random random;
    Visitor visitor;

    public static Visitor BuildValidAndComplete() {
        return new VisitorBuilder()
                .withId()
                .withFirstName()
                .withLastName()
                .withPatient()
                .withPhoneNumber()
                .withAppointment(ScheduleState.VALID)
                .withEntry(ScheduleState.VALID)
                .build();
    }

    public VisitorBuilder() {
        this.random = new Random();
        this.visitor = new Visitor();
    }

    public Visitor build() {
        Visitor visitor = new Visitor();
        visitor.setAppointment(this.visitor.getAppointment());
        visitor.setId(this.visitor.getId());
        visitor.setEntry(this.visitor.getEntry());
        visitor.setFirstName(this.visitor.getFirstName());
        visitor.setLastName(this.visitor.getLastName());
        visitor.setPatient(this.visitor.getPatient());
        visitor.setPhoneNumber(this.visitor.getPhoneNumber());
        return visitor;
    }

    public VisitorBuilder withAppointment(ScheduleState state) {
        switch (state) {
            case VALID:
                withValidAppointment();
                break;
            case AFTER:
                withAppointmentAfterAllowed();
                break;
            case BEFORE:
                withAppointmentBeforeAllowed();
                break;
        }

        return this;
    }

    public VisitorBuilder withEntry(ScheduleState state) {
        LocalDate today = LocalDate.now();
        LocalTime appointment = this.visitor.getAppointment();
        LocalDateTime entry = LocalDateTime.of(
                today.getYear(),
                today.getMonth(),
                today.getDayOfMonth(),
                appointment.getHour(),
                appointment.getMinute());

        switch (state) {
            case VALID:
                withValidEntry(entry);
                break;
            case BEFORE:
                withEntryBeforeAllowed(entry);
                break;
            case AFTER:
                withEntryAfterAllowed(entry);
                break;
        }

        return this;
    }

    public VisitorBuilder withFirstName() {
        this.visitor.setLastName(FIRST_NAMES[random.nextInt(FIRST_NAMES.length)]);
        return this;
    }

    public VisitorBuilder withId() {
        this.visitor.setId(random.nextLong());
        return this;
    }

    public VisitorBuilder withLastName() {
        this.visitor.setLastName(LAST_NAMES[random.nextInt(LAST_NAMES.length)]);
        return this;
    }

    public VisitorBuilder withPatient() {
        this.visitor.setPatient(new PatientBuilder()
                .withAdmission()
                .withDepartment()
                .withCode()
                .build());
        return this;
    }

    public VisitorBuilder withPhoneNumber() {
        this.visitor.setPhoneNumber(PHONE_NUMBERS[random.nextInt(PHONE_NUMBERS.length)]);
        return this;
    }

    private void withAppointmentBeforeAllowed() {
        this.visitor.setAppointment(VisitorScheduleUtil.START_TIME.minusMinutes(10));
    }

    private void withAppointmentAfterAllowed() {
        this.visitor.setAppointment(VisitorScheduleUtil.END_TIME.plusMinutes(10));
    }

    private void withEntryAfterAllowed(LocalDateTime entryDateTime) {
        this.visitor.setEntry(entryDateTime.plusMinutes(20));
    }

    private void withEntryBeforeAllowed(LocalDateTime entryDateTime) {
        this.visitor.setEntry(entryDateTime.minusMinutes(20));
    }

    private void withValidAppointment() {
        int validSeconds = VisitorScheduleUtil.END_TIME.toSecondOfDay()
                - VisitorScheduleUtil.START_TIME.toSecondOfDay();
        int validMinutes = random.nextInt(validSeconds / 60 / 10) * 10;
        this.visitor.setAppointment(VisitorScheduleUtil.START_TIME.plusMinutes(validMinutes));
    }

    private void withValidEntry(LocalDateTime entryDateTime) {
        int minutes = random.nextInt(30) + 1;
        this.visitor.setEntry(entryDateTime.plusMinutes(15).minusMinutes(minutes));
    }
}