package moonduck.calendar.simple.service;

import java.time.LocalTime;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import moonduck.calendar.simple.entity.Meeting;

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
}
