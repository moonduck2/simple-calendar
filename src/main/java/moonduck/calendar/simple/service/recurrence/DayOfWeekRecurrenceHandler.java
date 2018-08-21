package moonduck.calendar.simple.service.recurrence;

import java.time.LocalDate;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;

public class DayOfWeekRecurrenceHandler implements RecurrenceHandler {

	@Override
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence) {
		// TODO Auto-generated method stub
		return false;
	}
}
