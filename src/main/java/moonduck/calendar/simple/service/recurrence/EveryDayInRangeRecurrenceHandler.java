package moonduck.calendar.simple.service.recurrence;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.entity.Recurrence;
import moonduck.calendar.simple.enumeration.RecurrenceType;

@Service
public class EveryDayInRangeRecurrenceHandler implements RecurrenceHandler {

	@Override
	public boolean isOccur(LocalDate date, Meeting meeting, Recurrence recurrence) {
		LocalDate start = meeting.getStartDate();
		LocalDate end = meeting.getEndDate();
		return !date.isBefore(start) && !date.isAfter(end);
	}

	@Override
	public RecurrenceType getAvailableType() {
		return RecurrenceType.EVERYDAY_IN_RANGE;
	}

	@Override
	public LocalDate nextOccur(LocalDate baseDate, Recurrence recurrence) {
		return baseDate.plusDays(1);
	}

}
