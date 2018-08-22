package moonduck.calendar.simple.util;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.Test;

public class RecurrenceCalculatorTest {

	@Test
	public void 시작_날짜부터_n회_반복_후_날짜_계산_테스트() {
		LocalDate startDay = LocalDate.of(2018, 8, 20); //월요일
		DayOfWeek wednesday = DayOfWeek.WEDNESDAY;
		int recurrence = 3;
		assertEquals(LocalDate.of(2018, 9, 5), RecurrenceCalculator.calcLastDate(startDay, wednesday, recurrence));
		
		startDay = LocalDate.of(2018, 8, 23); //목요일
		assertEquals(LocalDate.of(2018, 9, 12), RecurrenceCalculator.calcLastDate(startDay, wednesday, recurrence));
		
		startDay = LocalDate.of(2018, 8, 22); //수요일
		assertEquals(LocalDate.of(2018, 9, 5), RecurrenceCalculator.calcLastDate(startDay, wednesday, recurrence));
	}
}
