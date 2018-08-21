package moonduck.calendar.simple.type;

import java.time.LocalDate;

public interface HasDateRange {
	public LocalDate getStartDate();
	public LocalDate getEndDate();
}
