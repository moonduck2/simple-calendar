package moonduck.calendar.simple.service.recurrence;

import java.time.LocalDate;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;

public interface RecurrenceHandler {
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence);
}
