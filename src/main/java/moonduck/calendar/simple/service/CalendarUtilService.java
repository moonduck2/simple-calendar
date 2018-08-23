package moonduck.calendar.simple.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.dto.MeetingDto;
import moonduck.calendar.simple.dto.RecurrenceDto;
import moonduck.calendar.simple.entity.Meeting;
import moonduck.calendar.simple.enumeration.RecurrenceType;
import moonduck.calendar.simple.util.TemporalCalculator;

/**
 * service 로직에서 공통적으로 혹은 유틸성으로 사용될만한 메소드 모음
 * static 메소드로 하는게 적절하나 테스트 설정이 까다로워 mocking이 쉬운 일반 bean으로 함(개선 필요)
 */
@Service
public class CalendarUtilService {
	/**
	 * 
	 * @param meetings 특정 날짜의 모든 회의
	 * @param startTime 시간대가 겹쳤는지 확인하기 위한 기준 시작시간
	 * @param endTime 시간대가 겹쳤는지 확인하기 위한 기준 종료시간
	 * @return baseMeeting과 겹쳐있는 시간대 회의를 modifiedTime이 적은 순서로 정렬된 자료구조
	 */
	public SortedSet<Meeting> findDuplicatedPeriod(List<Meeting> meetings, 
			LocalTime baseStartTime, LocalTime baseEndTime) {
		return meetings.stream()
				.filter(meeting -> {
					LocalTime startTime = meeting.getStartTime();
					LocalTime endTime = meeting.getEndTime();		
					if (endTime.isBefore(baseStartTime) || endTime.equals(baseStartTime)
							|| startTime.equals(baseEndTime) || startTime.isAfter(baseEndTime)) {
						return false;
					} else {
						return true;
					}
				}).collect(Collectors.toCollection(() -> new TreeSet<>( //버전 순 정렬
						(a, b) -> Long.compare(a.getModifiedTime(), b.getModifiedTime()))));
	}
	
	/**
	 * 문제의 단순화를 위해 아래 값을 요청한 값과는 다른 값으로 보정한다.
	 * 1. startDate -> 최초 회의가 있는 날짜(반복설정에 해당하는 요일의 날짜)
	 * 2. endDate -> 최종 회의가 있는 날짜(반복설정에 해당하는 마지막 횟수의 날짜)
	 * 3. recurrence -> 없을 경우 1회 반복으로 생성
	 * @param meeting 
	 * @return 정상화된 DTO
	 */
	public MeetingDto normalizeMeeting(MeetingDto meeting) {
		setRecurrenceIfNotExist(meeting);
		calcRealStartDay(meeting);
		calcRealEndDate(meeting);
		return meeting;
	}
	
	private MeetingDto calcRealStartDay(MeetingDto meeting) {
		meeting.setStartDate(TemporalCalculator.calcFirstDate(meeting.getStartDate(), DayOfWeek.of(meeting.getRecurrence().getDayOfWeek())));
		return meeting;
	}
	
	private MeetingDto setRecurrenceIfNotExist(MeetingDto meeting) {
		RecurrenceDto recur = meeting.getRecurrence();
		if (recur == null) {
			//문제의 단순화를 위해 반복이 없는 경우는 1회 반복으로 처리한다.
			recur = new RecurrenceDto().setType(RecurrenceType.ONCE_A_WEEK)
				.setCount(1)
				//반복이 없는 경우는 startDate와 endDate가 같으므로 둘 중 어느 하나의 요일을 선택해도 무관하나
				//startDate가 null인 경우는 절대 없으므로 항상 존재하는 값인 startDate의 요일을 선택
				.setDayOfWeek(meeting.getStartDate().getDayOfWeek().getValue());
			meeting.setRecurrence(recur);
		}
		return meeting;
	}
	
	private MeetingDto calcRealEndDate(MeetingDto meeting) {
		RecurrenceDto recur = meeting.getRecurrence();
		meeting.setEndDate(TemporalCalculator.calcLastDate(
				meeting.getStartDate(), DayOfWeek.of(recur.getDayOfWeek()), recur.getCount()));
		return meeting;
	}
}
