package moonduck.calendar.simple.service.recurrence;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;

@Service
public class DayOfWeekRecurrenceHandler implements RecurrenceHandler {

	@Override
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence) {
		LocalDate startDate = meeting.getStart();
		LocalDate endDate = meeting.getEnd();
		if (date.isBefore(startDate) || date.isAfter(endDate)) {
			return false;
		}
		return true;
	}
}
