package moonduck.calendar.simple.util;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.Test;

import moonduck.calendar.simple.util.TemporalCalculator;

public class TemporalCalculatorTest {
	
	@Test
	public void 시작_날짜부터_n회_반복_후_날짜_계산_테스트() {
		LocalDate startDay = LocalDate.of(2018, 8, 20); //월요일
		DayOfWeek wednesday = DayOfWeek.WEDNESDAY;
		int recurrence = 3;
		assertEquals(LocalDate.of(2018, 9, 5), TemporalCalculator.calcLastDate(startDay, wednesday, recurrence));
		
		startDay = LocalDate.of(2018, 8, 23); //목요일
		assertEquals(LocalDate.of(2018, 9, 12), TemporalCalculator.calcLastDate(startDay, wednesday, recurrence));
		
		startDay = LocalDate.of(2018, 8, 22); //수요일
		assertEquals(LocalDate.of(2018, 9, 5), TemporalCalculator.calcLastDate(startDay, wednesday, recurrence));
	}
	
	@Test
	public void 특정_날짜로부터_가장_가까운_특정_요일의_날짜_구하기_테스트() {
		LocalDate startDay = LocalDate.of(2018, 8, 20);
		DayOfWeek wednesday = DayOfWeek.WEDNESDAY;
		
		assertEquals(LocalDate.of(2018, 8, 22), TemporalCalculator.calcFirstDate(startDay, wednesday));
		
		startDay = LocalDate.of(2018, 8, 23);
		assertEquals(LocalDate.of(2018, 8, 29), TemporalCalculator.calcFirstDate(startDay, wednesday));
		
		startDay = LocalDate.of(2018, 8, 22);
		assertEquals(startDay, TemporalCalculator.calcFirstDate(startDay, wednesday));
	}
}
