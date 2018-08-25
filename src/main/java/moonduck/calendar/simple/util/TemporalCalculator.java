package moonduck.calendar.simple.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public final class TemporalCalculator {
	private TemporalCalculator() {}
	
	
	/**
	 * 특정일로부터 특정 요일의 n번 반복 후 마지막 날짜를 리턴한다
	 * @param start 기준일자
	 * @param dayOfWeek 기준요일
	 * @param recurrence 반복횟수
	 * @return 시작일을 포함해서 반복 후 마지막 날짜
	 */
	public static LocalDate calcLastDate(LocalDate start, DayOfWeek dayOfWeek, Integer recurrence) {
		return start.getDayOfWeek() == dayOfWeek ? start.plusDays(7 * (recurrence - 1)) : //시작일 포함
				start.with(TemporalAdjusters.next(dayOfWeek)).plusDays(7 * (recurrence - 1));
	}
	
	/**
	 * 특정일에서 가장 가까운 특정 요일의 날짜를 찾는다.
	 * @param start 기준일
	 * @param dayOfWeek 기준요일
	 * @return 가장 가까운 dayOfWeek의 날짜
	 */
	public static LocalDate calcFirstDate(LocalDate start, DayOfWeek dayOfWeek) {
		if (start.getDayOfWeek() == dayOfWeek) {
			return start;
		}
		return start.with(TemporalAdjusters.next(dayOfWeek));
	}
}
