package moonduck.calendar.simple.service.recurrence;

import java.time.LocalDate;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;

public interface RecurrenceHandler {
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence);
	public RecurrenceType getAvailableType();
	public LocalDate nextOccur(LocalDate baseDate, Recurrence recurrence);
}
