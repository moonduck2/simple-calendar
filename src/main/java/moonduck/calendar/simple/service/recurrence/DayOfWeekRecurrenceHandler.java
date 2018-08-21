package moonduck.calendar.simple.service.recurrence;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;

@Service
public class DayOfWeekRecurrenceHandler implements RecurrenceHandler {

	@Override
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence) {
		LocalDate startDate = meeting.getStartDate();
		LocalDate endDate = meeting.getEndDate();
		if (date.isBefore(startDate) || date.isAfter(endDate)) {
			return false;
		}
		
		return date.getDayOfWeek().getValue() == recurrence.getDayOfWeek();
	}

	@Override
	public RecurrenceType getAvailableType() {
		return RecurrenceType.ONCE_A_WEEK;
	}
}
