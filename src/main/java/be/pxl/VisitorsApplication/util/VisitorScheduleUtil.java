package be.pxl.VisitorsApplication.util;

import be.pxl.VisitorsApplication.util.exception.InvalidSheduleTimeException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VisitorScheduleUtil {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	public static final LocalTime START_TIME = LocalTime.of(8, 30);
	public static final LocalTime END_TIME = LocalTime.of(19, 30);

	public static void validateVisitationMoment(LocalTime entryTime) throws InvalidSheduleTimeException {
		if (entryTime.isBefore(START_TIME)) {
			throw new InvalidSheduleTimeException("The visit must occur after " + TIME_FORMATTER.format(START_TIME) + ".");
		}
		if (entryTime.isAfter(END_TIME)) {
			throw new InvalidSheduleTimeException("The visit must occur before  " + TIME_FORMATTER.format(END_TIME) + ".");
		}
		if (entryTime.getMinute() % 10 != 0) {
			throw new InvalidSheduleTimeException("The visit can only occur in intervals of 10 minutes.");
		}
	}


	public static void validateEntryMoment(LocalDateTime EntryTime, LocalTime sheduledTime) throws InvalidSheduleTimeException {
		LocalDateTime validEntryMoment = LocalDateTime.of(EntryTime.toLocalDate(), sheduledTime);
		LocalDateTime quarterHourBefore = validEntryMoment.minusMinutes(15);
		LocalDateTime QuarterHourAfter = validEntryMoment.plusMinutes(15);

		if (EntryTime.isBefore(quarterHourBefore)) {
			throw new InvalidSheduleTimeException("You are too early. Please wait outside.");
		}
		if (EntryTime.isAfter(QuarterHourAfter)) {
			throw new InvalidSheduleTimeException("You are too late. Please report at reception.");
		}
	}

}
