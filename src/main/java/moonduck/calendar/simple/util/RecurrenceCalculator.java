package moonduck.calendar.simple.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public final class RecurrenceCalculator {
	private RecurrenceCalculator() {}
	
	public static LocalDate calcLastDate(LocalDate start, DayOfWeek dayOfWeek, int recurrence) {
		return start.getDayOfWeek() == dayOfWeek ? start.plusDays(7 * (recurrence - 1)) : //시작일 포함
				start.with(TemporalAdjusters.next(dayOfWeek)).plusDays(7 * (recurrence - 1));
	}
}
